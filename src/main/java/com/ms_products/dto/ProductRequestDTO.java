package com.ms_products.ms_products.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotNull(message = "KPI value is required")
    private String code;

    @NotNull(message = "Category cannot be null")
    private String category;

    @Positive(message = "Price must be greater than zero")
    private Double price;

    @Min(value = 0, message = "Stock cannot be less than zero")
    private Integer stock;


}