package com.ms_products.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.HashSet;

/**
 * Entity representing product categories.
 * <p>
 * This class maps to the 'categories' table and maintains a
 * many-to-many relationship with products.
 * </p>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class CategoryEntity {

    /**
     * Unique identifier for the category.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique name of the category.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    /**
     * Brief description of what the category encompasses.
     */
    @Column(length = 255)
    private String description;

    /**
     * Status flag to indicate if the category is currently available.
     * Defaults to true for new instances.
     */
    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    /**
     * Set of products associated with this category.
     * Managed by the 'categories' field in the ProductEntity.
     */
    @Builder.Default
    @ManyToMany(mappedBy = "categories")
    @ToString.Exclude // Prevents infinite recursion in logs
    private Set<ProductEntity> products = new HashSet<>();

    /**
     * Standard implementation for entity equality based on ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryEntity that)) return false;
        return id != null && id.equals(that.getId());
    }

    /**
     * Standard implementation for entity hash code.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}