package com.eventtracker.exceptions;

public class KafkaPublishingException extends RuntimeException {
    public KafkaPublishingException(String message, Throwable cause) {
        super(message, cause);
    }
}