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
    @Column(name = "idempotency_key", nullable = false)
    private Long idempotencyKey; // Cambiado a Long para coincidir con el DTO

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity; // Cambiado para que sea el objeto, no solo el ID

    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "status_code")
    private int statusCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}