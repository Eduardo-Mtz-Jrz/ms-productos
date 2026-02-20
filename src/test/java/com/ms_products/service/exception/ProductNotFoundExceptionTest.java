package com.ms_products.service.exception;

import com.ms_products.exception.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for ProductNotFoundException to ensure full coverage.
 */
class ProductNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with custom string message")
    void productNotFoundExceptionStringConstructorTest() {
        String message = "Custom error message";
        ProductNotFoundException exception = new ProductNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Should create exception with ID-based message")
    void productNotFoundExceptionLongConstructorTest() {
        Long id = 100L;
        ProductNotFoundException exception = new ProductNotFoundException(id);

        assertEquals("Product with ID 100 not found.", exception.getMessage());
    }
}