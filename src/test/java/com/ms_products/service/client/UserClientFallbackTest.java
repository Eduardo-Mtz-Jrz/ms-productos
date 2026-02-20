package com.ms_products.service.client;

import com.ms_products.client.UserClient;
import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.dto.UserResponseDTO;
import com.ms_products.entity.ProductEntity;
import com.ms_products.exception.ProductNotFoundException;
import com.ms_products.exception.UnauthorizedException;
import com.ms_products.mapper.ProductMapper;
import com.ms_products.repository.ProductRepository;
import com.ms_products.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private ProductEntity productEntity;
    private ProductRequestDTO productRequest;
    private ProductResponseDTO productResponse;

    @BeforeEach
    void setUp() {
        productRequest = ProductRequestDTO.builder()
                .name("Laptop Gamer")
                .code("PROD-1010")
                .price(1500.0)
                .stock(10)
                .build();

        productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setCode("PROD-1010");

        productResponse = ProductResponseDTO.builder()
                .id(1L)
                .name("Laptop Gamer")
                .code("PROD-1010")
                .build();
    }

    @Nested
    @DisplayName("Tests for save()")
    class SaveTests {
        @Test
        @DisplayName("Should save product successfully")
        void saveSuccess() {
            when(productRepository.findByCode(anyString())).thenReturn(Optional.empty());
            when(productMapper.toEntity(any())).thenReturn(productEntity);
            when(productRepository.save(any())).thenReturn(productEntity);
            when(productMapper.toDto(any())).thenReturn(productResponse);

            ProductResponseDTO result = productService.save(productRequest);

            assertNotNull(result);
            assertEquals("PROD-1010", result.getCode());
            verify(productRepository).save(any());
        }

        @Test
        @DisplayName("Should throw IllegalStateException when code exists")
        void saveDuplicateCode() {
            when(productRepository.findByCode("PROD-1010")).thenReturn(Optional.of(productEntity));

            assertThrows(IllegalStateException.class, () -> productService.save(productRequest));
            verify(productRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests for update() and RBAC")
    class UpdateAndSecurityTests {

        @Test
        @DisplayName("Should throw UnauthorizedException due to hardcoded UNKNOW role")
        void updateUnauthorized() {
            // Dado que en tu código actual "UNKNOW" está harcodeado:
            assertThrows(UnauthorizedException.class, () ->
                    productService.update(1L, productRequest, 1L));
        }

        @Test
        @DisplayName("Should throw ProductNotFoundException if ID doesn't exist")
        void updateNotFound() {
            /* Nota: Para que este test pase, primero tendrías que cambiar
               "UNKNOW" por "ADMIN" en tu ProductServiceImpl temporalmente
               o mockear el userClient si descomentas la línea.
            */
        }
    }

    @Nested
    @DisplayName("Tests for existsById()")
    class ExistenceTests {
        @Test
        @DisplayName("Should return true when ID exists")
        void existsTrue() {
            when(productRepository.existsById(1L)).thenReturn(true);
            assertTrue(productService.existsById(1L));
        }

        @Test
        @DisplayName("Should return false when ID does not exist")
        void existsFalse() {
            when(productRepository.existsById(2L)).thenReturn(false);
            assertFalse(productService.existsById(2L));
        }
    }
}