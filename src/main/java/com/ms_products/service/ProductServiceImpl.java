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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link ProductService} for managing product lifecycle.
 * Includes caching strategies and admin-level validation.
 *
 * @author Angel Gabriel
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserClient userClient;
    private final ProductMapper productMapper;

    private static final String CACHE_VALUE = "products";

    @Override
    @Transactional
    public ProductResponseDTO save(ProductRequestDTO request) {
        log.info("Process: Create product with code: {}", request.getCode());

        productRepository.findByCode(request.getCode())
                .ifPresent(p -> {
                    log.error("Conflict: Product code {} already exists", request.getCode());
                    throw new IllegalStateException("The product code is already registered in the system");
                });

        ProductEntity entity = productMapper.toEntity(request);
        return productMapper.toDto(productRepository.save(entity));
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_VALUE, key = "#id")
    public ProductResponseDTO update(Long id, ProductRequestDTO request, Long userId) {
        log.info("Process: Update product ID: {} by User: {}", id, userId);

        checkAdminPrivileges(userId);

        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productMapper.updateEntityFromDto(request, existingProduct);
        return productMapper.toDto(productRepository.save(existingProduct));
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_VALUE, key = "#id")
    public void delete(Long id) {
        log.info("Process: Delete product ID: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_VALUE, key = "#id")
    public ProductResponseDTO findById(Long id) {
        log.debug("Database fetch for product ID: {}", id);
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findLowStock(Integer threshold) {
        log.info("Filtering products with stock below threshold: {}", threshold);
        return productRepository.findByStockLessThan(threshold).stream()
                .map(productMapper::toDto)
                .toList();
    }

    /**
     * Validates if the user has administrative rights through UserClient.
     * @param userId The user ID to verify.
     * @throws UnauthorizedException if the user is not an admin or service fails.
     */
    private void checkAdminPrivileges(Long userId) {
        Boolean isAdmin = userClient.isAdmin(userId);
        if (!Boolean.TRUE.equals(isAdmin)) {
            log.warn("Security Alert: Unauthorized access attempt by user {}", userId);
            throw new UnauthorizedException("User lacks administrative permissions");
        }
    }
}