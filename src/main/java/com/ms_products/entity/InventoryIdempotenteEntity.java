package com.ms_products.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "inventory_idempotency")
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryIdempotenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idempotency_key")
    private Long idempotencyKey;

    @ManyToOne
    private ProductEntity productEntity;

    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "status_code")
    private int statusCode;

    @Column(name = "created_at")
    private Date createdAt;
}
