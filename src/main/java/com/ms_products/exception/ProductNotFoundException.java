package com.ms_products.ms_products.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Esto le dice a Spring que devuelva un 404
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product with ID " + id + " not found.");
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}