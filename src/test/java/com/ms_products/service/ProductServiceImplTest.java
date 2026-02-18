package com.ms_products.ms_products.service;

import com.ms_products.ms_products.client.UserClient;
import com.ms_products.ms_products.dto.ProductRequestDTO;
import com.ms_products.ms_products.dto.ProductResponseDTO;
import com.ms_products.ms_products.entity.ProductEntity;
import com.ms_products.ms_products.exception.ProductNotFoundException;
import com.ms_products.ms_products.exception.UnauthorizedException;
import com.ms_products.ms_products.mapper.ProductMapper;
import com.ms_products.ms_products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
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

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private UserClient userClient;
    @Mock private ProductMapper productMapper;

    @InjectMocks private ProductServiceImpl productService;

    private ProductEntity productEntity;
    private ProductRequestDTO requestDTO;
    private ProductResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setCode("P01");

        requestDTO = new ProductRequestDTO();
        requestDTO.setCode("P01");

        responseDTO = new ProductResponseDTO();
        responseDTO.setId(1L);
    }

    @Test
    void save_Success() {
        when(productRepository.findByCode(anyString())).thenReturn(Optional.empty());
        when(productMapper.toEntity(any())).thenReturn(productEntity);
        when(productRepository.save(any())).thenReturn(productEntity);
        when(productMapper.toDto(any())).thenReturn(responseDTO);

        ProductResponseDTO result = productService.save(requestDTO);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void save_Conflict_ThrowsException() {
        when(productRepository.findByCode(anyString())).thenReturn(Optional.of(productEntity));
        assertThrows(IllegalStateException.class, () -> productService.save(requestDTO));
    }

    @Test
    void update_Success_AsAdmin() {
        when(userClient.isAdmin(100L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any())).thenReturn(productEntity);
        when(productMapper.toDto(any())).thenReturn(responseDTO);

        ProductResponseDTO result = productService.update(1L, requestDTO, 100L);
        assertNotNull(result);
        verify(productMapper).updateEntityFromDto(any(), any());
    }

    @Test
    void update_Forbidden_ThrowsException() {
        when(userClient.isAdmin(100L)).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> productService.update(1L, requestDTO, 100L));
    }

    @Test
    void update_NotFound_ThrowsException() {
        when(userClient.isAdmin(100L)).thenReturn(true);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.update(1L, requestDTO, 100L));
    }

    @Test
    void delete_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        productService.delete(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void delete_NotFound_ThrowsException() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> productService.delete(1L));
    }

    @Test
    void findById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
        when(productMapper.toDto(any())).thenReturn(responseDTO);
        assertNotNull(productService.findById(1L));
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    void findAll_Success() {
        when(productRepository.findAll()).thenReturn(List.of(productEntity));
        when(productMapper.toDto(any())).thenReturn(responseDTO);
        List<ProductResponseDTO> list = productService.findAll();
        assertFalse(list.isEmpty());
    }
}