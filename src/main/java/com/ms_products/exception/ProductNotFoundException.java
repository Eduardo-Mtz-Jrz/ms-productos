package com.ms_products.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested product cannot be found in the system.
 * <p>
 * This class is annotated with {@link ResponseStatus} to automatically
 * return an HTTP 404 status code when thrown during web requests.
 * </p>
 *
 * @author Angel Gabriel
 * @version 1.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    /**
     * Constructs a new exception with a predefined message including
     * the product ID.
     *
     * @param id The unique identifier of the product that was not found.
     */
    public ProductNotFoundException(Long id) {
        super("Product with ID " + id + " not found.");
    }

    /**
     * Constructs a new exception with a custom detailed message.
     *
     * @param message The specific error message to be displayed.
     */
    public ProductNotFoundException(String message) {
        super(message);
    }
}