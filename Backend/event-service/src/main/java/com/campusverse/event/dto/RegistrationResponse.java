package com.campusverse.event.dto;

import com.campusverse.event.enums.RegistrationStatus;

import java.time.LocalDateTime;

public record RegistrationResponse(
        Long id,
        Long eventId,
        String eventTitle,
        Long studentId,
        RegistrationStatus status,
        LocalDateTime registeredAt
) {
}
