package com.eventtracker.exceptions;

public class ScoreFetchException extends RuntimeException {
    public ScoreFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}