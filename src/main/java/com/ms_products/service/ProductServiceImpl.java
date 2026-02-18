package com.ms_products.service;

import com.ms_products.ms_products.client.UserClient;
import com.ms_products.ms_products.dto.ProductRequestDTO;
import com.ms_products.ms_products.dto.ProductResponseDTO;
import com.ms_products.ms_products.entity.ProductEntity;
import com.ms_products.ms_products.exception.ProductNotFoundException;
import com.ms_products.ms_products.exception.UnauthorizedException;
import com.ms_products.ms_products.mapper.ProductMapper;
import com.ms_products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements com.ms_products.service.ProductService {

    private final ProductRepository productRepository;
    private final UserClient userClient;
    private final ProductMapper productMapper;

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

    @Override
    @Transactional
    public ProductResponseDTO update(Long id, ProductRequestDTO request, Long userId) {
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

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Attempting to delete ID: {}", id);
        if (!productRepository.existsById(id)) {
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
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }
}