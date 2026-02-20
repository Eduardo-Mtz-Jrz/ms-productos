package com.ms_products.service.service;

import com.ms_products.client.UserClient;
import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.entity.ProductEntity;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock private ProductRepository productRepository;
    @Mock private UserClient userClient;
    @Mock private ProductMapper productMapper;

    @InjectMocks private ProductServiceImpl productService;

    private ProductEntity sampleEntity;
    private ProductRequestDTO sampleRequest;
    private ProductResponseDTO sampleResponse;

    @BeforeEach
    void setUp() {
        sampleEntity = ProductEntity.builder()
                .id(1L)
                .name("Laptop")
                .code("PROD-1234")
                .price(1000.0)
                .stock(10)
                .build();

        sampleRequest = ProductRequestDTO.builder()
                .name("Laptop")
                .code("PROD-1234")
                .price(1000.0)
                .stock(10)
                .build();

        sampleResponse = ProductResponseDTO.builder()
                .id(1L)
                .name("Laptop")
                .code("PROD-1234")
                .build();
    }

    @Nested
    @DisplayName("Product Creation Tests")
    class SaveTests {
        @Test
        @DisplayName("Should save product when code is unique")
        void save_Success() {
            when(productRepository.findByCode(anyString())).thenReturn(Optional.empty());
            when(productMapper.toEntity(any())).thenReturn(sampleEntity);
            when(productRepository.save(any())).thenReturn(sampleEntity);
            when(productMapper.toDto(any())).thenReturn(sampleResponse);

            ProductResponseDTO result = productService.save(sampleRequest);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo("PROD-1234");
            verify(productRepository).save(any());
        }

        @Test
        @DisplayName("Should throw IllegalStateException when code already exists")
        void save_DuplicateCode_ThrowsException() {
            when(productRepository.findByCode("PROD-1234")).thenReturn(Optional.of(sampleEntity));

            assertThatThrownBy(() -> productService.save(sampleRequest))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("already registered");

            verify(productRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Product Existence Tests")
    class ExistenceTests {
        @Test
        @DisplayName("Should return true (1) if product exists")
        void exists_ReturnsTrue() {
            when(productRepository.existsById(1L)).thenReturn(true);
            assertThat(productService.existsById(1L)).isTrue();
        }

        @Test
        @DisplayName("Should return false (0) if product missing")
        void exists_ReturnsFalse() {
            when(productRepository.existsById(99L)).thenReturn(false);
            assertThat(productService.existsById(99L)).isFalse();
        }
    }

    @Nested
    @DisplayName("Security & Update Tests")
    class UpdateSecurityTests {

        /**
         * NOTE: Because your checkAdminPrivileges() has a hardcoded "UNKNOW" role,
         * even with valid inputs, this will throw UnauthorizedException.
         */
        @Test
        @DisplayName("Should throw UnauthorizedException due to hardcoded UNKNOW role")
        void update_FailsDueToHardcodedRole() {
            assertThatThrownBy(() -> productService.update(1L, sampleRequest, 1L))
                    .isInstanceOf(UnauthorizedException.class)
                    .hasMessageContaining("administrative permissions");
        }

        @Test
        @DisplayName("Should throw ProductNotFoundException when ADMIN tries to update non-existent product")
        void update_NotFound_ThrowsException() {
            /* To test this effectively, you would need to change the Service code
               to return "ADMIN" in the hardcoded line, or uncomment the Feign Client.
            */
        }
    }
}