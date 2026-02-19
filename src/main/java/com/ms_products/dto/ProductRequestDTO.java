package com.ms_products.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for incoming product requests.
 * <p>
 * This class captures the necessary data to create or update a product
 * entity, including comprehensive validation constraints to ensure
 * data integrity and security.
 * </p>
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    /**
     * The descriptive name of the product.
     */
    @NotBlank(message = "Product name must not be empty")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    /**
     * Unique identifier code for the product (KPI).
     * <p>Note: Required by 'codeNotNullTest'.</p>
     */
    @NotBlank(message = "KPI value is required")
    @Size(min = 3, max = 20, message = "Product code must be between 3 and 20 characters")
    private String code;

    /**
     * Category designation for catalog classification.
     * <p>Note: Required by 'categoryNotNullTest'.</p>
     */
    @NotBlank(message = "Category cannot be null")
    @Size(max = 50, message = "Category name is too long")
    private String category;

    /**
     * Unit price of the product. Must be a positive value.
     */
    @NotNull(message = "Price must be greater than zero")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    /**
     * Current inventory level. Cannot be negative.
     */
    @NotNull(message = "Stock cannot be less than zero")
    @Min(value = 0, message = "Stock cannot be less than zero")
    private Integer stock;
}