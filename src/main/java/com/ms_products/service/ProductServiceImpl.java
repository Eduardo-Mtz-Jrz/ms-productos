package com.ms_products.ms_products.service;

import com.ms_products.ms_products.client.UserClient;
import com.ms_products.ms_products.dto.ProductRequestDTO;
import com.ms_products.ms_products.dto.ProductResponseDTO;
import com.ms_products.ms_products.entity.ProductEntity;
import com.ms_products.ms_products.exception.ProductNotFoundException;
import com.ms_products.ms_products.exception.UnauthorizedException;
import com.ms_products.ms_products.mapper.ProductMapper;
import com.ms_products.ms_products.repository.ProductRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import lombok.extern.slf4j.Slf4j; // To view errors in console
import org.jspecify.annotations.NonNull;
=======
import lombok.extern.slf4j.Slf4j;
>>>>>>> 26f68f70136d294ea5c801c826d6f81f24c465e6
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserClient userClient;
    private final ProductMapper productMapper;

    @Override
<<<<<<< HEAD
    @CircuitBreaker(name = "productsCB", fallbackMethod = "fallbackAdd")
    @Retry(name = "productsRetry")
    public ProductEntity productAdd(@NonNull ProductEntity product) {
        productRepository.findByCode(product.getCode())
                .ifPresent(p -> {
                    throw new RuntimeException("Product code already exists");
                });
        return productRepository.save(product);
=======
    @Transactional
    public ProductResponseDTO save(ProductRequestDTO request) {
        log.info("Attempting to save product: {}", request.getCode());

        if (productRepository.findByCode(request.getCode()).isPresent()) {
            log.error("Save failed: Code {} exists", request.getCode());
            throw new IllegalStateException("Product code already exists");
        }

        ProductEntity product = productMapper.toEntity(request);
        return productMapper.toDto(productRepository.save(product));
>>>>>>> 26f68f70136d294ea5c801c826d6f81f24c465e6
    }

    @Override
<<<<<<< HEAD
    @CircuitBreaker(name = "productsCB", fallbackMethod = "fallbackUpdate")
    public ProductEntity productUpdate(Long id, @NonNull ProductEntity product) {
=======
    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO request, Long userId) {
        log.info("Update requested for ID: {} by user: {}", id, userId);

        if (!Boolean.TRUE.equals(userClient.isAdmin(userId))) {
            log.warn("Access denied for user {}", userId);
            throw new UnauthorizedException("User does not have admin privileges");
        }

>>>>>>> 26f68f70136d294ea5c801c826d6f81f24c465e6
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productMapper.updateEntityFromDto(request, existingProduct);
        return productMapper.toDto(productRepository.save(existingProduct));
    }

    @Override
<<<<<<< HEAD
    @CircuitBreaker(name = "productsCB", fallbackMethod = "fallbackDelete")
    public void productDelete(Long id) {
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(existingProduct);
=======
    @Transactional
    public void delete(Long id) {
        log.info("Attempting to delete ID: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
>>>>>>> 26f68f70136d294ea5c801c826d6f81f24c465e6
    }

    @Override
<<<<<<< HEAD
    @CircuitBreaker(name = "productsCB", fallbackMethod = "fallbackGetById")
    public ProductEntity productGetById(Long id) {
=======
    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
>>>>>>> 26f68f70136d294ea5c801c826d6f81f24c465e6
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
<<<<<<< HEAD
    @CircuitBreaker(name = "productsCB", fallbackMethod = "fallbackGetAll")
    public List<ProductEntity> productGetAll() {
        return productRepository.findAll();
    }

    // ===================================================================
    // FALLBACK METHODS (Executed only if the service fails or is down)
    // ===================================================================

    public ProductEntity fallbackAdd(ProductEntity p, Throwable t) {
        log.error("Fallback Add: Database service is not responding.");
        return new ProductEntity();
    }

    public ProductEntity fallbackUpdate(Long id, ProductEntity p, Throwable t) {
        log.error("Fallback Update: Error updating ID {}", id);
        return new ProductEntity();
    }

    public void fallbackDelete(Long id, Throwable t) {
        log.error("Fallback Delete: Could not delete ID {}", id);
    }

    public ProductEntity fallbackGetById(Long id, Throwable t) {
        log.error("Fallback GetById: ID {} not found", id);
        return new ProductEntity();
    }

    public List<ProductEntity> fallbackGetAll(Throwable t) {
        log.error("Fallback GetAll: Returning empty list for safety.");
        return new ArrayList<>();
    }
=======
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }
>>>>>>> 26f68f70136d294ea5c801c826d6f81f24c465e6
}