package com.campusverse.event.exception;

/** A business rule is violated (already registered, event cancelled...). Mapped to HTTP 409 Conflict. */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
