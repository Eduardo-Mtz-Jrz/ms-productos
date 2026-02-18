package com.ms_products.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user lacks the required authorization to
 * perform an action.
 * <p>
 * This exception is mapped to HTTP 403 Forbidden, indicating that
 * while the server understands the request, it refuses to authorize
 * it (e.g., a non-admin attempting an administrative task).
 * </p>
 *
 * @author Angel Gabriel
 * @version 1.0
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedException extends RuntimeException {

    /**
     * Constructs a new exception with a custom detailed message.
     *
     * @param message The detailed error message describing the
     * authorization failure.
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}