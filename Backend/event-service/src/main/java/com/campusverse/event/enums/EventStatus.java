package com.campusverse.event.enums;

/**
 * Lifecycle of an event.
 * DRAFT -> PENDING_ROOM (waiting for the Reservations service) -> PUBLISHED -> COMPLETED
 * Any event that is not completed can become CANCELLED.
 */
public enum EventStatus {
    DRAFT,
    PENDING_ROOM,
    PUBLISHED,
    CANCELLED,
    COMPLETED
}
