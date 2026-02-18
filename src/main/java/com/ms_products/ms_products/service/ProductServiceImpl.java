package com.ms_products.ms_products.service;

import com.ms_products.ms_products.client.UserClient;
import com.ms_products.ms_products.dto.ProductRequestDTO;
import com.ms_products.ms_products.dto.ProductResponseDTO;
import com.ms_products.ms_products.entity.ProductEntity;
import com.ms_products.ms_products.exception.ProductNotFoundException;
import com.ms_products.ms_products.exception.UnauthorizedException;
import com.ms_products.ms_products.mapper.ProductMapper;
import com.ms_products.ms_products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserClient userClient;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDTO save(ProductRequestDTO request) {
        log.info("Attempting to save product with code: {}", request.getCode());

        productRepository.findByCode(request.getCode())
                .ifPresent(p -> {
                    log.error("Save failed: Code {} already exists", request.getCode());
                    throw new IllegalStateException("Product code already exists");
                });

        ProductEntity product = productMapper.toEntity(request);
        ProductEntity savedProduct = productRepository.save(product);

        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO request, Long userId) {
        log.info("Update requested for product ID: {} by user ID: {}", id, userId);

        // Validación de seguridad: Boolean.TRUE.equals es la forma más segura contra nulls
        if (!Boolean.TRUE.equals(userClient.isAdmin(userId))) {
            log.warn("Access denied for user {}: Not an admin", userId);
            throw new UnauthorizedException("User does not have admin privileges");
        }

        return productRepository.findById(id)
                .map(existingProduct -> {
                    // Usamos el mapper para actualizar los campos automáticamente
                    productMapper.updateEntityFromDto(request, existingProduct);
                    ProductEntity updatedProduct = productRepository.save(existingProduct);
                    log.info("Product ID: {} updated successfully", id);
                    return productMapper.toDto(updatedProduct);
                })
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Attempting to delete product ID: {}", id);
        if (!productRepository.existsById(id)) {
            log.error("Delete failed: Product ID {} not found", id);
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }
}