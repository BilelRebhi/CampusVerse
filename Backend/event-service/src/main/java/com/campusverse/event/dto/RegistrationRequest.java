package com.campusverse.event.dto;

import jakarta.validation.constraints.NotNull;

/** Body of POST /api/events/{id}/registrations */
public record RegistrationRequest(
        @NotNull(message = "studentId is required")
        Long studentId
) {
}
