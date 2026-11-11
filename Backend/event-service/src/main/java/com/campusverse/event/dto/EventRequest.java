package com.campusverse.event.dto;

import com.campusverse.event.enums.EventCategory;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/** Data sent by the client to create or update an event. */
public record EventRequest(

        @NotBlank(message = "title is required")
        @Size(max = 150, message = "title must be at most 150 characters")
        String title,

        @Size(max = 2000, message = "description must be at most 2000 characters")
        String description,

        @NotNull(message = "category is required")
        EventCategory category,

        @NotNull(message = "startDateTime is required")
        @Future(message = "startDateTime must be in the future")
        LocalDateTime startDateTime,

        @NotNull(message = "endDateTime is required")
        LocalDateTime endDateTime,

        LocalDateTime registrationDeadline,

        @NotNull(message = "maxCapacity is required")
        @Positive(message = "maxCapacity must be greater than 0")
        Integer maxCapacity,

        @NotNull(message = "organizerId is required")
        Long organizerId,

        Long clubId,

        Long spaceId
) {
}
