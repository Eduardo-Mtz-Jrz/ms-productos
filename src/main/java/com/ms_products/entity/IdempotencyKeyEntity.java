package com.ms_products.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_keys")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotencyKeyEntity {

    @Id
    @Column(name = "idempotency_key", nullable = false, length = 100)
    private String key;

    @Column(name = "response_id", nullable = false)
    private Long responseId; // Aquí guardamos el ID del producto u orden creada

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}