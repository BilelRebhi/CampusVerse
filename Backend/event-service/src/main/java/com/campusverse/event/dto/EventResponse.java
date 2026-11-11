package com.campusverse.event.dto;

import com.campusverse.event.enums.EventCategory;
import com.campusverse.event.enums.EventStatus;

import java.time.LocalDateTime;

/** Data returned to the client for an event. */
public record EventResponse(
        Long id,
        String title,
        String description,
        EventCategory category,
        EventStatus status,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        LocalDateTime registrationDeadline,
        Integer maxCapacity,
        long confirmedCount,
        long availableSeats,
        Long organizerId,
        Long clubId,
        Long spaceId,
        Long reservationId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
