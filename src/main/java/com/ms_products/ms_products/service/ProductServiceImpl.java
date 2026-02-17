package com.ms_products.ms_products.service;

import com.ms_products.ms_products.entity.ProductEntity;
import com.ms_products.ms_products.repository.ProductRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // To view errors in console
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // ADD PRODUCT
    @Override
    @CircuitBreaker(name = "productsCB", fallbackMethod = "fallbackAdd")
    @Retry(name = "productsRetry")
    public ProductEntity productAdd(@NonNull ProductEntity product) {
        productRepository.findByCode(product.getCode())
                .ifPresent(p -> {
                    throw new RuntimeException("Product code already exists");
                });
        return productRepository.save(product);
    }

    // UPDATE PRODUCT
    @Override
    @CircuitBreaker(name = "productsCB", fallbackMethod = "fallbackUpdate")
    public ProductEntity productUpdate(Long id, @NonNull ProductEntity product) {
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setCode(product.getCode());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategory(product.getCategory());

        return productRepository.save(existingProduct);
    }

    // DELETE PRODUCT
    @Override
    @CircuitBreaker(name = "productsCB", fallbackMethod = "fallbackDelete")
    public void productDelete(Long id) {
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(existingProduct);
    }

    // GET PRODUCT BY ID
    @Override
    @CircuitBreaker(name = "productsCB", fallbackMethod = "fallbackGetById")
    public ProductEntity productGetById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // GET ALL PRODUCTS
    @Override
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
}