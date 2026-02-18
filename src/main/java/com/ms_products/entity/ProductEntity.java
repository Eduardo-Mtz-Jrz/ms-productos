package com.ms_products.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "")
    @Size(min = 3, max = 100)
    private String name;

    @NotBlank(message = "")
    @Pattern(regexp = "^PROD-\\d{4}$")
    private String code;

    @NotNull(message = "")
    @DecimalMin(value = "0.01")
    private Double price;

    @Min(value = 0)
    private Integer stock;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<CategoryEntity> categories = new HashSet<>();
}