package com.eventtracker.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(KafkaPublishingException.class)
    public ResponseEntity<String> handleKafkaPublishingException(KafkaPublishingException ex) {
        log.error("KafkaPublishingException caught: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Temporary messaging error: " + ex.getMessage());
    }

    @ExceptionHandler(PollingException.class)
    public ResponseEntity<String> handlePollingException(PollingException ex) {
        log.error("PollingException caught: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body("Polling service unavailable: " + ex.getMessage());
    }

    @ExceptionHandler(ScoreFetchException.class)
    public ResponseEntity<String> handleScoreFetchException(ScoreFetchException ex) {
        log.error("ScoreFetchException caught: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Failed to fetch live score: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}", errorMessage);
        return ResponseEntity.badRequest().body("Validation failed: " + errorMessage);
    }

}