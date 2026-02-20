package com.ms_products.repository;

import com.ms_products.entity.InventoryIdempotencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryIdempotencyRepository extends JpaRepository<InventoryIdempotencyEntity, Long> {
    Optional<InventoryIdempotencyEntity> findByIdempotencyKey(String idempotencyKey);
}