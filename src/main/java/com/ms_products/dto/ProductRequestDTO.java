package com.ms_products.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Product name must not be empty")
    private String name;

    @NotBlank(message = "KPI value is required") // El test 'codeNotNullTest' espera que no sea vacío
    private String code;

    @NotBlank(message = "Category cannot be null") // El test 'categoryNotNullTest' espera que no sea vacío
    private String category;

    @NotNull(message = "Price must be greater than zero")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @NotNull(message = "Stock cannot be less than zero")
    @Min(value = 0, message = "Stock cannot be less than zero")
    private Integer stock;
}