package com.campusverse.event.service;

import com.campusverse.event.dto.RegistrationResponse;
import com.campusverse.event.entity.Event;
import com.campusverse.event.entity.EventRegistration;
import com.campusverse.event.enums.EventStatus;
import com.campusverse.event.enums.RegistrationStatus;
import com.campusverse.event.exception.BusinessRuleException;
import com.campusverse.event.exception.ResourceNotFoundException;
import com.campusverse.event.mapper.EventMapper;
import com.campusverse.event.repository.EventRegistrationRepository;
import com.campusverse.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;

    /**
     * R3: event must be PUBLISHED and the deadline not passed.
     * R4: no double registration.
     * R5: when the event is full, the student goes on the waiting list.
     */
    @Override
    public RegistrationResponse register(Long eventId, Long studentId) {
        Event event = lockEvent(eventId);

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new BusinessRuleException("Registrations are only open for PUBLISHED events (current: "
                    + event.getStatus() + ")");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = event.getRegistrationDeadline() != null
                ? event.getRegistrationDeadline() : event.getStartDateTime();
        if (now.isAfter(deadline)) {
            throw new BusinessRuleException("Registration deadline has passed");
        }

        EventRegistration registration = registrationRepository.findByEventIdAndStudentId(eventId, studentId)
                .orElse(null);
        if (registration != null && registration.getStatus() != RegistrationStatus.CANCELLED) {
            throw new BusinessRuleException("Student " + studentId + " is already registered for event " + eventId);
        }

        long seatsTaken = registrationRepository.countByEventIdAndStatusIn(eventId, EventServiceImpl.SEAT_STATUSES);
        RegistrationStatus status = seatsTaken < event.getMaxCapacity()
                ? RegistrationStatus.CONFIRMED
                : RegistrationStatus.WAITLISTED;

        if (registration == null) {
            registration = EventRegistration.builder()
                    .event(event)
                    .studentId(studentId)
                    .status(status)
                    .build();
        } else {
            // the student had cancelled before and registers again
            registration.setStatus(status);
            registration.setRegisteredAt(now);
        }
        return EventMapper.toResponse(registrationRepository.save(registration));
    }

    /** R6: when a confirmed student cancels, the first waitlisted student gets the seat. */
    @Override
    public void cancelRegistration(Long eventId, Long studentId) {
        Event event = lockEvent(eventId);
        EventRegistration registration = getRegistration(eventId, studentId);

        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            throw new BusinessRuleException("Registration is already cancelled");
        }
        if (registration.getStatus() == RegistrationStatus.ATTENDED) {
            throw new BusinessRuleException("Cannot cancel: the student already attended the event");
        }
        boolean freedASeat = registration.getStatus() == RegistrationStatus.CONFIRMED;
        registration.setStatus(RegistrationStatus.CANCELLED);
        registrationRepository.save(registration);

        if (freedASeat && event.getStatus() == EventStatus.PUBLISHED) {
            registrationRepository
                    .findFirstByEventIdAndStatusOrderByRegisteredAtAsc(eventId, RegistrationStatus.WAITLISTED)
                    .ifPresent(next -> {
                        next.setStatus(RegistrationStatus.CONFIRMED);
                        registrationRepository.save(next);
                    });
        }
    }

    @Override
    public RegistrationResponse markAttendance(Long eventId, Long studentId) {
        getEventOrThrow(eventId);
        EventRegistration registration = getRegistration(eventId, studentId);
        if (registration.getStatus() != RegistrationStatus.CONFIRMED) {
            throw new BusinessRuleException("Only CONFIRMED registrations can be marked as attended (current: "
                    + registration.getStatus() + ")");
        }
        registration.setStatus(RegistrationStatus.ATTENDED);
        return EventMapper.toResponse(registrationRepository.save(registration));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationResponse> findByEvent(Long eventId) {
        getEventOrThrow(eventId);
        return registrationRepository.findByEventIdOrderByRegisteredAtAsc(eventId).stream()
                .map(EventMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationResponse> findByStudent(Long studentId) {
        return registrationRepository.findByStudentIdOrderByRegisteredAtDesc(studentId).stream()
                .map(EventMapper::toResponse)
                .toList();
    }

    // ------------------------------------------------------------------ helpers

    private Event lockEvent(Long eventId) {
        return eventRepository.findByIdForUpdate(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event " + eventId + " not found"));
    }

    private Event getEventOrThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event " + eventId + " not found"));
    }

    private EventRegistration getRegistration(Long eventId, Long studentId) {
        return registrationRepository.findByEventIdAndStudentId(eventId, studentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student " + studentId + " is not registered for event " + eventId));
    }
}
