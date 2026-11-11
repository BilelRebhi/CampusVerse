package com.campusverse.event.exception;

/** Invalid input that bean validation cannot detect (e.g. end date before start date). Mapped to HTTP 400. */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
