package com.campusverse.event.exception;

/** The user is not allowed to perform this action. Mapped to HTTP 403 Forbidden. */
public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException(String message) {
        super(message);
    }
}
