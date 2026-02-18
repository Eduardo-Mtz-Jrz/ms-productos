package com.ms_products.service;

import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;

import java.util.List;

/**
 * Service interface for managing product business logic.
 * <p>
 * This interface defines the contract for CRUD operations and administrative
 * validations for the product catalog.
 * </p>
 *
 * @author Angel Gabriel
 * @version 1.0
 */
public interface ProductService {

    /**
     * Creates and persists a new product in the system.
     *
     * @param request DTO containing the details of the
     *                product to be created.
     * @return {@link ProductResponseDTO} the newly created
     * product including its generated ID.
     * @throws jakarta.validation.ValidationException if the
     * request data is invalid.
     */
    ProductResponseDTO save(ProductRequestDTO request);

    /**
     * Updates an existing product's information.
     *
     * @param id      Unique identifier of the product to update.
     * @param request DTO containing the updated product details.
     * @param userId  ID of the user performing the update (used
     *                for admin privilege validation).
     * @return {@link ProductResponseDTO} the updated product data.
     * @throws RuntimeException if the product ID is not found or
     * the user lacks permissions.
     */
    ProductResponseDTO update(Long id, ProductRequestDTO request, Long userId);

    /**
     * Permanently removes a product from the system.
     *
     * @param id Unique identifier of the product to delete.
     * @throws RuntimeException if the product ID does not exist.
     */
    void delete(Long id);

    /**
     * Retrieves detailed information of a specific product by its ID.
     *
     * @param id Unique identifier of the product.
     * @return {@link ProductResponseDTO} the found product data.
     * @throws RuntimeException if no product is found with the given ID.
     */
    ProductResponseDTO findById(Long id);

    /**
     * Retrieves a list of all products registered in the system.
     *
     * @return A {@link List} of {@link ProductResponseDTO}.
     * Returns an empty list if no products exist.
     */
    List<ProductResponseDTO> findAll();
}