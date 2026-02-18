package com.ms_products.service;

import com.ms_products.client.UserClient;
import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.entity.ProductEntity;
import com.ms_products.exception.ProductNotFoundException;
import com.ms_products.exception.UnauthorizedException;
import com.ms_products.mapper.ProductMapper;
import com.ms_products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link ProductService} providing business logic
 * for product management.
 * <p>
 * This service handles CRUD operations, integrates with external user
 * validation services, and manages data persistence via the
 * {@link ProductRepository}.
 * </p>
 *
 * @author Angel Gabriel
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserClient userClient;
    private final ProductMapper productMapper;

    /**
     * Creates and persists a new product in the database.
     *
     * @param request DTO containing the product details to be saved.
     * @return {@link ProductResponseDTO} representing the newly
     * created product.
     * @throws IllegalStateException if a product with the same code
     * already exists.
     */
    @Override
    @Transactional
    public ProductResponseDTO save(ProductRequestDTO request) {
        log.info("Attempting to save product: {}", request.getCode());

        if (productRepository.findByCode(request.getCode()).isPresent()) {
            log.error("Save failed: Code {} exists", request.getCode());
            throw new IllegalStateException("Product code already exists");
        }

        ProductEntity product = productMapper.toEntity(request);
        return productMapper.toDto(productRepository.save(product));
    }

    /**
     * Updates an existing product after verifying administrative
     * privileges.
     *
     * @param id      The unique identifier of the product to update.
     * @param request Updated product data.
     * @param userId  ID of the user performing the update for
     * authorization check.
     * @return The updated {@link ProductResponseDTO}.
     * @throws UnauthorizedException    if the user does not have
     * administrative rights.
     * @throws ProductNotFoundException if no product is found
     * with the provided ID.
     */
    @Override
    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO request,
                                     Long userId) {
        log.info("Update requested for ID: {} by user: {}", id, userId);

        if (!Boolean.TRUE.equals(userClient.isAdmin(userId))) {
            log.warn("Access denied for user {}", userId);
            throw new UnauthorizedException("User does not have admin privileges");
        }

        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productMapper.updateEntityFromDto(request, existingProduct);
        return productMapper.toDto(productRepository.save(existingProduct));
    }

    /**
     * Deletes a product from the system by its ID.
     *
     * @param id The unique identifier of the product to be deleted.
     * @throws ProductNotFoundException if the product does not exist.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Attempting to delete ID: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Retrieves a specific product by its unique identifier.
     *
     * @param id The ID of the product to find.
     * @return The found {@link ProductResponseDTO}.
     * @throws ProductNotFoundException if the ID is not present
     * in the database.
     */
    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    /**
     * Retrieves all products currently stored in the system.
     *
     * @return A {@link List} of all products as DTOs.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }
}