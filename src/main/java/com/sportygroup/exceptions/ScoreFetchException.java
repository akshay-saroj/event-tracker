package com.sportygroup.exceptions;

public class ScoreFetchException extends RuntimeException {
    public ScoreFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}