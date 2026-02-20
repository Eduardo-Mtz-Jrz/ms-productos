package com.ms_products.service;

import com.ms_products.client.UserClient;
import com.ms_products.dto.IdempotencyRequestDTO;
import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.entity.IdempotencyKeyEntity;
import com.ms_products.entity.ProductEntity;
import com.ms_products.enums.TypeEnum;
import com.ms_products.exception.ProductNotFoundException;
import com.ms_products.exception.UnauthorizedException;
import com.ms_products.mapper.ProductMapper;
import com.ms_products.repository.InventoryIdempotencyRepository;
import com.ms_products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventoryIdempotencyRepository idempotencyRepository;
    private final UserClient userClient;
    private final ProductMapper productMapper;

    private static final String CACHE_VALUE = "products";

    @Override
    @Transactional
    public Boolean registerOrder(IdempotencyRequestDTO request) {
        return idempotencyRepository.findById(request.idEpotrncyKey())
                .map(existing -> true)
                .orElseGet(() -> {
                    ProductEntity product = productRepository.findById(request.productId())
                            .orElseThrow(() -> new ProductNotFoundException(request.productId()));

                    int cantidadAjuste = request.quantity().intValue();

                    if (TypeEnum.IN.equals(request.type())) {
                        product.setStock(product.getStock() + cantidadAjuste);
                    } else {
                        product.setStock(product.getStock() - cantidadAjuste);
                    }

                    productRepository.save(product);

                    IdempotencyKeyEntity logIdempotencia = IdempotencyKeyEntity.builder()
                            .key(request.idEpotrncyKey().toString())
                            .responseId(product.getId())
                            .createdAt(LocalDateTime.now())
                            .build();

                    idempotencyRepository.save(logIdempotencia);
                    return true;
                });
    }

    @Override
    @Transactional
    public ProductResponseDTO save(ProductRequestDTO request) {
        productRepository.findByCode(request.getCode())
                .ifPresent(p -> {
                    throw new IllegalStateException("The product code is already registered");
                });
        ProductEntity entity = productMapper.toEntity(request);
        return productMapper.toDto(productRepository.save(entity));
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_VALUE, key = "#id")
    public ProductResponseDTO update(Long id, ProductRequestDTO request, Long userId) {
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
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_VALUE, key = "#id")
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

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findLowStock(Integer threshold) {
        return productRepository.findByStockLessThan(threshold).stream()
                .map(productMapper::toDto)
                .toList();
    }

    private void checkAdminPrivileges(Long userId) {
        Boolean isAdmin = userClient.isAdmin(userId);
        if (!Boolean.TRUE.equals(isAdmin)) {
            throw new UnauthorizedException("User lacks administrative permissions");
        }
    }
}