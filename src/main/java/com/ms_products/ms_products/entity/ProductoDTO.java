package com.ms_products.ms_products.entity;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductoDTO {

    @NotBlank(message = "El nombre es obligatorio y no puede estar vacío")
    @Pattern(regexp = "^\\S+$", message = "El nombre no debe contener espacios en blanco")
    private String name;

    @NotNull(message = "El precio es obligatorio")
    @Digits(integer = 10, fraction = 2, message = "El precio debe tener máximo 2 decimales")
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    @NotNull(message = "Sin piezas en existencia")
    @Min(value = 1, message = "En stock debe haber existencia")
    private Integer stock;

    // Getters y Setters
}