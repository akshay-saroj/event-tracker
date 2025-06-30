package com.sportygroup.exceptions;

public class PollingException extends RuntimeException {
    public PollingException(String message, Throwable cause) {
        super(message, cause);
    }
}