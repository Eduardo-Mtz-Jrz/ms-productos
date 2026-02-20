package com.ms_products.exception;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

        log.warn("Validation failed: {}", details);
        return buildResponse("Validation failed: " + details, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles cases where a requested product does not exist.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFound(ProductNotFoundException ex) {
        log.error("Product not found: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles security exceptions for unauthorized operations (RBAC).
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorized(UnauthorizedException ex) {
        log.error("Security violation: {}", ex.getMessage());
        // Usamos FORBIDDEN (403) porque el usuario está autenticado pero no tiene permisos
        return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Handles business logic conflicts (e.g., duplicate product codes).
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleConflict(IllegalStateException ex) {
        log.error("Conflict detected: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Fallback handler for any other unhandled exceptions.
     * Prevents leaking sensitive stack traces to the client.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        log.error("Unexpected error: ", ex);
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