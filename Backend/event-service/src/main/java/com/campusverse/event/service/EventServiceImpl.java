package com.campusverse.event.service;

import com.campusverse.event.client.CampusSpacesClient;
import com.campusverse.event.client.ReservationsClient;
import com.campusverse.event.client.StudentClubsClient;
import com.campusverse.event.client.dto.SpaceDTO;
import com.campusverse.event.dto.EventRequest;
import com.campusverse.event.dto.EventResponse;
import com.campusverse.event.entity.Event;
import com.campusverse.event.enums.EventCategory;
import com.campusverse.event.enums.EventStatus;
import com.campusverse.event.enums.RegistrationStatus;
import com.campusverse.event.exception.*;
import com.campusverse.event.mapper.EventMapper;
import com.campusverse.event.repository.EventRegistrationRepository;
import com.campusverse.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    /** Registrations that occupy a seat. */
    static final List<RegistrationStatus> SEAT_STATUSES =
            List.of(RegistrationStatus.CONFIRMED, RegistrationStatus.ATTENDED);

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final CampusSpacesClient campusSpacesClient;
    private final ReservationsClient reservationsClient;
    private final StudentClubsClient studentClubsClient;

    // ------------------------------------------------------------------ queries

    @Override
    @Transactional(readOnly = true)
    public List<EventResponse> findAll(EventCategory category, EventStatus status, Long clubId,
                                       LocalDateTime from, LocalDateTime to) {
        return eventRepository.findAll().stream()
                .filter(e -> category == null || e.getCategory() == category)
                .filter(e -> status == null || e.getStatus() == status)
                .filter(e -> clubId == null || clubId.equals(e.getClubId()))
                .filter(e -> from == null || !e.getStartDateTime().isBefore(from))
                .filter(e -> to == null || !e.getStartDateTime().isAfter(to))
                .sorted(Comparator.comparing(Event::getStartDateTime))
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse findById(Long id) {
        return toResponse(getEvent(id));
    }

    // ------------------------------------------------------------------ commands

    @Override
    public EventResponse create(EventRequest request) {
        validateDates(request);
        checkClubManager(request.clubId(), request.organizerId());
        checkRoomCapacity(request.spaceId(), request.maxCapacity());

        Event event = EventMapper.toEntity(request);
        event.setStatus(EventStatus.DRAFT);
        return toResponse(eventRepository.save(event));
    }

    @Override
    public EventResponse update(Long id, EventRequest request) {
        Event event = getEvent(id);
        ensureModifiable(event);
        validateDates(request);

        long seatsTaken = registrationRepository.countByEventIdAndStatusIn(id, SEAT_STATUSES);
        if (request.maxCapacity() < seatsTaken) {
            throw new BusinessRuleException("maxCapacity (" + request.maxCapacity()
                    + ") cannot be lower than the number of confirmed participants (" + seatsTaken + ")");
        }
        boolean roomOrTimeChanged = !Objects.equals(event.getSpaceId(), request.spaceId())
                || !event.getStartDateTime().equals(request.startDateTime())
                || !event.getEndDateTime().equals(request.endDateTime());
        if (event.getReservationId() != null && roomOrTimeChanged) {
            throw new BusinessRuleException("The room is already reserved: cancel the event to change its room or time");
        }
        if (!Objects.equals(event.getClubId(), request.clubId())) {
            checkClubManager(request.clubId(), request.organizerId());
        }
        checkRoomCapacity(request.spaceId(), request.maxCapacity());

        EventMapper.updateEntity(event, request);
        return toResponse(eventRepository.save(event));
    }

    @Override
    public void delete(Long id) {
        Event event = getEvent(id);
        if (event.getStatus() != EventStatus.DRAFT) {
            throw new BusinessRuleException("Only DRAFT events can be deleted; cancel the event instead");
        }
        eventRepository.delete(event);
    }

    /**
     * Publishes the event. If it has a room, the room is first reserved in the Reservations service.
     * If Reservations is down, the event is saved as PENDING_ROOM (not lost) and a 503 is returned.
     */
    @Override
    @Transactional(noRollbackFor = ExternalServiceException.class)
    public EventResponse publish(Long id) {
        Event event = getEvent(id);
        if (event.getStatus() != EventStatus.DRAFT && event.getStatus() != EventStatus.PENDING_ROOM) {
            throw new BusinessRuleException("Only DRAFT or PENDING_ROOM events can be published (current: "
                    + event.getStatus() + ")");
        }
        if (event.getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("Cannot publish an event that has already started");
        }

        if (event.getSpaceId() != null && event.getReservationId() == null) {
            event.setStatus(EventStatus.PENDING_ROOM);
            eventRepository.saveAndFlush(event);

            Long reservationId = reservationsClient.reserveRoom(event); // may throw 503 -> stays PENDING_ROOM
            event.setReservationId(reservationId);
        }

        event.setStatus(EventStatus.PUBLISHED);
        try {
            return toResponse(eventRepository.saveAndFlush(event));
        } catch (RuntimeException e) {
            // Compensation: the room was booked but we could not save -> release it
            reservationsClient.cancelReservation(event.getReservationId());
            throw e;
        }
    }

    @Override
    public EventResponse cancel(Long id) {
        Event event = getEvent(id);
        if (event.getStatus() == EventStatus.CANCELLED || event.getStatus() == EventStatus.COMPLETED) {
            throw new BusinessRuleException("Event is already " + event.getStatus());
        }
        reservationsClient.cancelReservation(event.getReservationId());
        event.setStatus(EventStatus.CANCELLED);
        // Later: publish an async "EventCancelled" message so participants are notified
        return toResponse(eventRepository.save(event));
    }

    // ------------------------------------------------------------------ helpers

    Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event " + id + " not found"));
    }

    private EventResponse toResponse(Event event) {
        long confirmed = event.getId() == null ? 0
                : registrationRepository.countByEventIdAndStatusIn(event.getId(), SEAT_STATUSES);
        return EventMapper.toResponse(event, confirmed);
    }

    private void ensureModifiable(Event event) {
        if (event.getStatus() == EventStatus.CANCELLED || event.getStatus() == EventStatus.COMPLETED) {
            throw new BusinessRuleException("A " + event.getStatus() + " event cannot be modified");
        }
    }

    /** R1 + R2 */
    private void validateDates(EventRequest request) {
        if (!request.endDateTime().isAfter(request.startDateTime())) {
            throw new InvalidRequestException("endDateTime must be after startDateTime");
        }
        if (request.registrationDeadline() != null
                && request.registrationDeadline().isAfter(request.startDateTime())) {
            throw new InvalidRequestException("registrationDeadline must be before startDateTime");
        }
    }

    /** R9: only a manager of the club can create a club event. */
    private void checkClubManager(Long clubId, Long organizerId) {
        if (clubId != null && !studentClubsClient.isClubManager(clubId, organizerId)) {
            throw new ForbiddenOperationException("User " + organizerId + " is not a manager of club " + clubId);
        }
    }

    /** R7: event capacity cannot exceed room capacity. If Campus Spaces is down, the check is skipped. */
    private void checkRoomCapacity(Long spaceId, Integer maxCapacity) {
        if (spaceId == null) {
            return;
        }
        try {
            Optional<SpaceDTO> space = campusSpacesClient.getSpace(spaceId);
            space.ifPresent(s -> {
                if (s.capacity() != null && maxCapacity > s.capacity()) {
                    throw new BusinessRuleException("maxCapacity (" + maxCapacity
                            + ") exceeds the capacity of room " + s.name() + " (" + s.capacity() + ")");
                }
            });
        } catch (ExternalServiceException e) {
            log.warn("Campus Spaces unavailable, room capacity check skipped for space {}", spaceId);
        }
    }
}
