package com.ms_products.ms_products.service;

import com.ms_products.ms_products.dto.ProductRequestDTO;
import com.ms_products.ms_products.dto.ProductResponseDTO;

import java.util.List;

/**
 * Service interface for managing product business logic.
 */
public interface ProductService {

    ProductResponseDTO save(ProductRequestDTO request);

    /**
     * Updates an existing product.
     * * @param id Product identifier
     * @param request Data to update
     * @param userId ID of the user performing the update (for admin validation)
     * @return The updated product DTO
     */
    ProductResponseDTO update(Long id, ProductRequestDTO request, Long userId);

    void delete(Long id);

    ProductResponseDTO findById(Long id);

    List<ProductResponseDTO> findAll();
}