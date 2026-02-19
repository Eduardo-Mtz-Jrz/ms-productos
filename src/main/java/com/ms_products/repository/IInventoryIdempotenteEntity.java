package com.ms_products.repository;

import com.ms_products.entity.InventoryIdempotenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInventoryIdempotenteEntity extends JpaRepository<InventoryIdempotenteEntity, Long> {
}
