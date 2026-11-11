package com.campusverse.event.client.dto;

import java.time.LocalDateTime;

/** Body sent to MS2 Reservations: POST /api/reservations */
public record ReservationRequestDTO(
        Long spaceId,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Long requesterId,
        String purpose,
        Long eventId
) {
}
