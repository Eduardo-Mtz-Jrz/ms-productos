package com.ms_products.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global exception handler for the product microservice.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String MESSAGE = "message";
    private static final String STATUS = "status";

    /**
     * REQUERIMIENTO SWAGGER: Status 400
     * Devuelve el texto plano exacto solicitado.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Validation failed for request");
        // Según tu Swagger, la respuesta debe ser este texto plano exacto
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid data or missing headers");
    }

    /**
     * Requerimiento Swagger: Si falta un header requerido (como Idempotency-Key)
     * También debe responder con 400 y el texto plano.
     */
    @ExceptionHandler(org.springframework.web.bind.MissingRequestHeaderException.class)
    public ResponseEntity<String> handleMissingHeaders(org.springframework.web.bind.MissingRequestHeaderException ex) {
        log.warn("Missing header: {}", ex.getHeaderName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid data or missing headers");
    }

    /**
     * Maneja el caso de producto no encontrado (404).
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Object> handleProductNotFound(ProductNotFoundException ex) {
        log.error("Product not found: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de seguridad (403).
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorized(UnauthorizedException ex) {
        log.error("Security violation: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    /**
     * Maneja conflictos de lógica de negocio (409).
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleConflict(IllegalStateException ex) {
        log.error("Conflict detected: {}", ex.getMessage());
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * Fallback para errores internos (500).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        log.error("Unexpected error: ", ex);
        return buildResponse("An unexpected error occurred on the server.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Método utilitario para JSON (usado solo en 404, 403, 409 y 500).
     */
    private ResponseEntity<Object> buildResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(MESSAGE, message);
        body.put(STATUS, status.value());
        return new ResponseEntity<>(body, status);
    }
}