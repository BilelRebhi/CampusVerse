package com.campusverse.event.mapper;

import com.campusverse.event.dto.EventRequest;
import com.campusverse.event.dto.EventResponse;
import com.campusverse.event.dto.RegistrationResponse;
import com.campusverse.event.entity.Event;
import com.campusverse.event.entity.EventRegistration;

/** Converts between entities and DTOs (the API never exposes entities directly). */
public final class EventMapper {

    private EventMapper() {
    }

    public static Event toEntity(EventRequest request) {
        Event event = new Event();
        updateEntity(event, request);
        return event;
    }

    public static void updateEntity(Event event, EventRequest request) {
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setCategory(request.category());
        event.setStartDateTime(request.startDateTime());
        event.setEndDateTime(request.endDateTime());
        event.setRegistrationDeadline(request.registrationDeadline());
        event.setMaxCapacity(request.maxCapacity());
        event.setOrganizerId(request.organizerId());
        event.setClubId(request.clubId());
        event.setSpaceId(request.spaceId());
    }

    public static EventResponse toResponse(Event event, long confirmedCount) {
        long available = Math.max(0, event.getMaxCapacity() - confirmedCount);
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getCategory(),
                event.getStatus(),
                event.getStartDateTime(),
                event.getEndDateTime(),
                event.getRegistrationDeadline(),
                event.getMaxCapacity(),
                confirmedCount,
                available,
                event.getOrganizerId(),
                event.getClubId(),
                event.getSpaceId(),
                event.getReservationId(),
                event.getCreatedAt(),
                event.getUpdatedAt());
    }

    public static RegistrationResponse toResponse(EventRegistration registration) {
        return new RegistrationResponse(
                registration.getId(),
                registration.getEvent().getId(),
                registration.getEvent().getTitle(),
                registration.getStudentId(),
                registration.getStatus(),
                registration.getRegisteredAt());
    }
}
