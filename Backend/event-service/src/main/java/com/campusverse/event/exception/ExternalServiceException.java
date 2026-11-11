package com.campusverse.event.exception;

/** Another microservice is unreachable or failed. Mapped to HTTP 503 Service Unavailable. */
public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalServiceException(String message) {
        super(message);
    }
}
