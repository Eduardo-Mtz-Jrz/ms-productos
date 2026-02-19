package com.ms_products.repository;

import com.ms_products.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link ProductEntity} operations.
 * <p>
 * Provides standard CRUD functionality and custom query methods
 * for product-specific business requirements.
 * </p>
 *
 * @author Angel Gabriel
 * @version 1.1
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    /**
     * Retrieves a product by its unique business code.
     *
     * @param code The unique string code of the product.
     * @return An {@link Optional} containing the found product,
     * or empty if none matches.
     */
    Optional<ProductEntity> findByCode(String code);

    /**
     * Finds all products whose stock level is strictly less than
     * the given threshold.
     * <p>
     * This method supports the "Low Stock" business requirement.
     * </p>
     *
     * @param threshold The maximum stock limit (exclusive).
     * @return A {@link List} of products with low inventory.
     */
    List<ProductEntity> findByStockLessThan(Integer threshold);
}