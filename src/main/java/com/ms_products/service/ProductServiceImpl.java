package com.ms_products.service;

import com.ms_products.client.UserClient;
import com.ms_products.dto.MovementRequestDTO;
import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.dto.UserResponseDTO;
import com.ms_products.entity.InventoryIdempotencyEntity;
import com.ms_products.entity.ProductEntity;
import com.ms_products.exception.ProductNotFoundException;
import com.ms_products.exception.UnauthorizedException;
import com.ms_products.mapper.ProductMapper;
import com.ms_products.repository.InventoryIdempotencyRepository;
import com.ms_products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link ProductService} for managing product lifecycle.
 * Includes caching strategies and RBAC (Role-Based Access Control) validation.
 *
 * @author Angel Gabriel
 * @version 1.2
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserClient userClient;
    private final ProductMapper productMapper;

    private static final String CACHE_VALUE = "products";
    private static final String ADMIN_ROLE = "ADMIN";

    @Override
    @Transactional
    public ProductResponseDTO save(ProductRequestDTO request) {
        log.info("Process: Creating product with code: {}", request.getCode());

        productRepository.findByCode(request.getCode())
                .ifPresent(p -> {
                    log.error("Conflict: Product code {} already exists", request.getCode());
                    throw new IllegalStateException("The product code is already registered in the system");
                });

        ProductEntity entity = productMapper.toEntity(request);
        return productMapper.toDto(productRepository.save(entity));
    }

    @Override
    public Boolean registerOrder(MovementRequestDTO idempotenteRequestDTO) {
        return null;
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_VALUE, key = "#id")
    public ProductResponseDTO update(Long id, ProductRequestDTO request, Long userId) {
        log.info("Process: Updating product ID: {} by User: {}", id, userId);

        // RBAC Validation
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
        log.info("Process: Deleting product ID: {}", id);
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
     * Checks if a product exists in the database.
     * This implementation provides a lightweight check to be used by the controller
     * to return binary responses (1/0) instead of throwing exceptions.
     *
     * @param id Unique identifier of the product.
     * @return true if the product exists, false otherwise.
     */
    @Override
    @Transactional(readOnly = true)
    public Boolean existsById(Long id) {
        log.debug("Checking existence for product ID: {}", id);
        return productRepository.existsById(id);
    }

    /**
     * Validates if the user has administrative rights by comparing the retrieved role.
     * @param userId The unique identifier of the user to verify.
     * @throws UnauthorizedException if the user lacks ADMIN role or if the user is not found.
     */
    private void checkAdminPrivileges(Long userId) {
        UserResponseDTO user = userClient.getUserById(userId);

        // Fail-safe validation: Check for null response or incorrect role
        if (user == null || !ADMIN_ROLE.equalsIgnoreCase(user.getRole())) {
            log.warn("Security Alert: Unauthorized access attempt by user {}. Role found: {}",
                    userId, (user != null ? user.getRole() : "UNKNOWN"));

            throw new UnauthorizedException("User lacks administrative permissions to perform this action");
        }

        log.info("Authorization successful: User {} verified with role {}", userId, user.getRole());
    }


    private final InventoryIdempotencyRepository idempotencyRepository;
    @Transactional
    public ResponseEntity<String> processInventoryMovement(Long productId, Integer quantity, ProductRequestDTO dto) {
        // Generar la llave: CATEGORIA-CODIGO-PRECIO
        String generatedKey = String.format("%s-%s-%s",
                dto.getCategory().toUpperCase(),
                dto.getCode(),
                dto.getPrice());

        // 1. Lógica: Si key ya existe -> NO modificar stock. Retornar 200.
        var existingRecord = idempotencyRepository.findByIdempotencyKey(generatedKey);
        if (existingRecord.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Key already exists. Stock NOT modified");
        }

        // 2. Si no existe: Buscar producto
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));

        // 3. Ejecutar movimiento
        product.setStock(product.getStock() + quantity);
        productRepository.save(product);

        // 4. Registrar idempotency_key
        InventoryIdempotencyEntity record = InventoryIdempotencyEntity.builder()
                .idempotencyKey(generatedKey)
                .productId(productId)
                .build();
        idempotencyRepository.save(record);

        // 5. Retornar 201 (Movement executed)
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Movement executed (New transaction)");
    }




}