package com.ms_products.repository;

import com.ms_products.entity.IdempotencyKeyEntity; // Importamos tu entidad real
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para gestionar las llaves de idempotencia.
 * Hereda de JpaRepository usando IdempotencyKeyEntity y Long como ID.
 */
@Repository
public interface InventoryIdempotencyRepository extends JpaRepository<IdempotencyKeyEntity, Long> {
}