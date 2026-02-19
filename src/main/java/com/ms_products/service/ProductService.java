package com.ms_products.service;

import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;

import java.util.List;

/**
 * Service interface for managing product business logic.
 * <p>
 * This interface defines the contract for CRUD operations, inventory
 * queries, and administrative validations for the product catalog.
 * </p>
 *
 * @author Angel Gabriel
 * @version 1.1
 */
public interface ProductService {

    /**
     * Creates and persists a new product in the system.
     *
     * @param request DTO containing the details of the product to be created.
     * @return {@link ProductResponseDTO} the newly created product.
     */
    ProductResponseDTO save(ProductRequestDTO request);

    /**
     * Updates an existing product's information.
     *
     * @param id      Unique identifier of the product to update.
     * @param request DTO containing the updated product details.
     * @param userId  ID of the user performing the update.
     * @return {@link ProductResponseDTO} the updated product data.
     */
    ProductResponseDTO update(Long id, ProductRequestDTO request, Long userId);

    /**
     * Permanently removes a product from the system.
     *
     * @param id Unique identifier of the product to delete.
     */
    void delete(Long id);

    /**
     * Retrieves detailed information of a specific product by its ID.
     *
     * @param id Unique identifier of the product.
     * @return {@link ProductResponseDTO} the found product data.
     */
    ProductResponseDTO findById(Long id);

    /**
     * Retrieves a list of all products registered in the system.
     *
     * @return A {@link List} of {@link ProductResponseDTO}.
     */
    List<ProductResponseDTO> findAll();

    /**
     * Retrieves products that have a stock level below the specified
     * threshold.
     * <p>
     * This method is used for inventory alerts and low-stock reporting.
     * </p>
     *
     * @param threshold The stock limit for filtering.
     * @return A {@link List} of products with low stock levels.
     */
    List<ProductResponseDTO> findLowStock(Integer threshold);
}