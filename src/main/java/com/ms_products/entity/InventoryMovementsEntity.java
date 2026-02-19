package com.ms_products.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity representing stock movements within the inventory system.
 * <p>
 * This class tracks every increase or decrease in product stock,
 * providing an audit trail for inventory management.
 * </p>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory_movements", indexes = {
        @Index(name = "idx_movement_product", columnList = "product_id"),
        @Index(name = "idx_movement_type", columnList = "movement_type")
})
public class InventoryMovementsEntity {

    /**
     * Unique identifier for the inventory movement record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The product associated with this stock movement.
     * Uses Lazy fetching to optimize memory usage.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude // Prevents potential issues during logging
    private ProductEntity product;

    /**
     * Type of movement (e.g., 'IN', 'OUT', 'ADJUSTMENT').
     */
    @Column(name = "movement_type", nullable = false, length = 20)
    private String movementType;

    /**
     * The number of units moved.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Detailed reason for the stock change.
     */
    @Column(length = 255)
    private String reason;

    /**
     * Timestamp of when the movement was recorded.
     * Added to meet auditing standards.
     */
    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Custom implementation of equals based on the ID for JPA consistency.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryMovementsEntity that)) return false;
        return id != null && id.equals(that.getId());
    }

    /**
     * Hash code based on class type for stable usage in collections.
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}