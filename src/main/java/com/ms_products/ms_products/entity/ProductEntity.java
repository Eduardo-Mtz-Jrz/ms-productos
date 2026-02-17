package com.ms_products.ms_products.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products") // nombre de la tabla en MySQL
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(message = "{}")
    private Long id;

    @NotBlank(message = "{}")
    @Size(min = 3, max = 100, message = "{}")
    private String name;

    @NotBlank(message = "{}")
    @Pattern(regexp = "^PROD-\\d{4}$", message = "{}")
    private String code;

    @NotNull(message = "{}")
    @DecimalMin(value = "0.01", message = "{}")
    private Double price;

    @Min(value = 0, message = "{}")
    private Integer stock;

    @NotBlank(message = "{}")
    @Size(min = 3, max = 50, message = "{}")
    private String category;
}
