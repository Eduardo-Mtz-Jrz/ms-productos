package com.ms_products.service.service;

import com.ms_products.client.UserClient;
import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.entity.ProductEntity;
import com.ms_products.exception.ProductNotFoundException;
import com.ms_products.exception.UnauthorizedException;
import com.ms_products.mapper.ProductMapper;
import com.ms_products.repository.ProductRepository;
import com.ms_products.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ProductServiceImpl}.
 * Covers all business logic, security checks, and branch coverage for JaCoCo.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequestDTO requestDTO;
    private ProductEntity productEntity;
    private ProductResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = ProductRequestDTO.builder()
                .code("P-100")
                .name("Test Product")
                .build();

        productEntity = ProductEntity.builder()
                .id(1L)
                .code("P-100")
                .name("Test Product")
                .build();

        responseDTO = ProductResponseDTO.builder()
                .id(1L)
                .code("P-100")
                .build();
    }

    // --- SAVE OPERATIONS ---

    @Test
    @DisplayName("Should successfully save a product when code is unique")
    void save_Success() {
        when(productRepository.findByCode(anyString())).thenReturn(Optional.empty());
        when(productMapper.toEntity(any())).thenReturn(productEntity);
        when(productRepository.save(any())).thenReturn(productEntity);
        when(productMapper.toDto(any())).thenReturn(responseDTO);

        ProductResponseDTO result = productService.save(requestDTO);

        assertNotNull(result);
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    @DisplayName("Should throw IllegalStateException when attempting to save a duplicate product code")
    void save_DuplicateCode_ThrowsException() {
        when(productRepository.findByCode("P-100")).thenReturn(Optional.of(productEntity));

        assertThrows(IllegalStateException.class, () -> productService.save(requestDTO));
        verify(productRepository, never()).save(any());
    }

    // --- UPDATE OPERATIONS ---

    @Test
    @DisplayName("Should update product details when user is admin and product exists")
    void update_Success() {
        Long productId = 1L;
        Long userId = 99L;

        when(userClient.isAdmin(userId)).thenReturn(true);
        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any())).thenReturn(productEntity);
        when(productMapper.toDto(any())).thenReturn(responseDTO);

        ProductResponseDTO result = productService.update(productId, requestDTO, userId);

        assertNotNull(result);
        verify(productMapper).updateEntityFromDto(requestDTO, productEntity);
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when a non-admin user attempts an update")
    void update_NotAdmin_ThrowsException() {
        Long userId = 99L;
        when(userClient.isAdmin(userId)).thenReturn(false);

        assertThrows(UnauthorizedException.class, () ->
                productService.update(1L, requestDTO, userId));

        verify(productRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when updating a non-existent product")
    void update_NotFound_ThrowsException() {
        Long productId = 1L;
        Long userId = 99L;
        when(userClient.isAdmin(userId)).thenReturn(true);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                productService.update(productId, requestDTO, userId));
    }

    // --- DELETE OPERATIONS ---

    @Test
    @DisplayName("Should delete product successfully when it exists")
    void delete_Success() {
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> productService.delete(id));
        verify(productRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when deleting an ID that does not exist")
    void delete_NotFound_ThrowsException() {
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.delete(id));
    }

    // --- RETRIEVAL OPERATIONS (Full Coverage) ---

    @Test
    @DisplayName("Should return a product DTO when finding by a valid ID")
    void findById_Success() {
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.of(productEntity));
        when(productMapper.toDto(any())).thenReturn(responseDTO);

        ProductResponseDTO result = productService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when ID is not found (Fixes yellow line)")
    void findById_NotFound_ThrowsException() {
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(id));
        verify(productMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Should return a list of all products (Fixes red line)")
    void findAll_Success() {
        when(productRepository.findAll()).thenReturn(List.of(productEntity));
        when(productMapper.toDto(any())).thenReturn(responseDTO);

        List<ProductResponseDTO> result = productService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("Should return low stock products (Fixes red line)")
    void findLowStock_Success() {
        Integer threshold = 10;
        when(productRepository.findByStockLessThan(threshold)).thenReturn(List.of(productEntity));
        when(productMapper.toDto(any())).thenReturn(responseDTO);

        List<ProductResponseDTO> result = productService.findLowStock(threshold);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productRepository).findByStockLessThan(threshold);
    }
}