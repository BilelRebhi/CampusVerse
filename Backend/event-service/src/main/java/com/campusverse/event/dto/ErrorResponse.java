package com.campusverse.event.dto;

import java.time.LocalDateTime;
import java.util.Map;

/** Standard error body returned by every endpoint. */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
}
