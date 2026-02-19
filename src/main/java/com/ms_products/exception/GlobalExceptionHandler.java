package com.ms_products.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the product microservice.
 * <p>
 * Standardizes all error responses to ensure security and consistency.
 * Captures business exceptions, validation errors, and unexpected server failures.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String MESSAGE = "message";
    private static final String STATUS = "status";

    /**
     * Handles validation errors from @Valid annotated DTOs.
     * Prevents 500 errors when the client sends invalid data.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return buildResponse("Validation failed: " + details, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles cases where a requested product does not exist.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFound(ProductNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles security exceptions for unauthorized operations.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorized(UnauthorizedException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Fallback handler for any other unhandled exceptions.
     * Prevents leaking sensitive stack traces to the client.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        return buildResponse("An unexpected error occurred on the server.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Utility method to construct a consistent JSON error body.
     */
    private ResponseEntity<Object> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(MESSAGE, message);
        body.put(STATUS, status.value());
        return new ResponseEntity<>(body, status);
    }
}