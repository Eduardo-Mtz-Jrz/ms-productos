package com.ms_products.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

/**
 * Entity representing the products catalog.
 * <p>
 * This class maps to the 'products' table and maintains a
 * many-to-many relationship with categories.
 * </p>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_product_code_unique", columnList = "code", unique = true)
})
public class ProductEntity {

    /**
     * Primary key for the product entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Commercial name of the product.
     */
    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Unique business code. Format: PROD-XXXX.
     */
    @NotBlank(message = "Product code is mandatory")
    @Pattern(regexp = "^PROD-\\d{4}$", message = "Invalid code format. Expected: PROD-0000")
    @Column(nullable = false, unique = true, length = 10)
    private String code;

    /**
     * Unit price of the product.
     */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    @Column(nullable = false)
    private Double price;

    /**
     * Current stock level in inventory.
     */
    @Min(value = 0, message = "Stock level cannot be negative")
    @Column(nullable = false)
    private Integer stock;

    /**
     * Set of categories associated with this product.
     * Uses Lazy fetching for better performance.
     */
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @ToString.Exclude
    private Set<CategoryEntity> categories = new HashSet<>();

    /**
     * Custom implementation of equals based on the ID for JPA consistency.
     * Required to avoid issues with Proxies and Sets.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    /**
     * HashCode implementation based on the entity class.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}