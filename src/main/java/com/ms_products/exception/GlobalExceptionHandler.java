package com.ms_products.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler for the product microservice.
 * <p>
 * This class uses {@link ControllerAdvice} to intercept exceptions thrown by controllers
 * across the entire application, providing a standardized JSON error response.
 * </p>
 *
 * @author Angel Gabriel
 * @version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles cases where a requested product does not exist in the database.
     *
     * @param ex The {@link ProductNotFoundException} thrown by the service layer.
     * @return A {@link ResponseEntity} with a 404 Not Found status and error details.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFound(ProductNotFoundException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Handles security exceptions when a user lacks the necessary permissions
     * to perform a specific action (e.g., non-admin attempting an update).
     *
     * @param ex The {@link UnauthorizedException} thrown during permission validation.
     * @return A {@link ResponseEntity} with a 403 Forbidden status and error details.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorized(UnauthorizedException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Utility method to construct a consistent error response structure.
     * <p>
     * The response body includes:
     * <ul>
     * <li><b>timestamp:</b> The exact time the error occurred.</li>
     * <li><b>message:</b> A descriptive error message.</li>
     * <li><b>status:</b> The HTTP status code value.</li>
     * </ul>
     * </p>
     *
     * @param message The error message to display to the client.
     * @param status  The {@link HttpStatus} code to be returned.
     * @return A formatted {@link ResponseEntity} containing the error map.
     */
    private ResponseEntity<Object> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        body.put("status", status.value());
        return new ResponseEntity<>(body, status);
    }
}