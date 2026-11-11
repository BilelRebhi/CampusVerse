package com.campusverse.event.client.dto;

/** Reservation returned by MS2 Reservations. */
public record ReservationDTO(
        Long id,
        Long spaceId,
        String status
) {
}
