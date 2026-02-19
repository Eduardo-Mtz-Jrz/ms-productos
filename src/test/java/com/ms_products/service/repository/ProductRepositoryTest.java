package com.ms_products.service.repository;

import com.ms_products.entity.ProductEntity;
import com.ms_products.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link ProductRepository}.
 * <p>
 * This class validates the persistence layer, ensuring that custom queries
 * and data integrity constraints behave as expected within the JPA context.
 * </p>
 */
@DataJpaTest
@ActiveProfiles("local")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Verifies that a product can be retrieved by its unique business code.
     * Expected format: PROD-XXXX.
     */
    @Test
    @DisplayName("Should find a product by its unique code following the PROD-XXXX pattern")
    void findByCodeTest() {
        // Arrange
        String targetCode = "PROD-1234";
        ProductEntity product = ProductEntity.builder()
                .code(targetCode)
                .name("Gamer Laptop")
                .price(1500.0)
                .stock(10)
                .categories(new HashSet<>())
                .build();
        productRepository.save(product);

        // Act
        Optional<ProductEntity> result = productRepository.findByCode(targetCode);

        // Assert
        assertTrue(result.isPresent(), "Product should be present in the database");
        result.ifPresent(p -> assertEquals("Gamer Laptop", p.getName(), "The retrieved product name should match"));
    }

    /**
     * Verifies that the repository correctly filters products with inventory
     * below a specific threshold.
     */
    @Test
    @DisplayName("Should list all products with stock lower than a given threshold")
    void findByStockLessThanTest() {
        // Arrange
        int threshold = 5;
        ProductEntity lowStock = ProductEntity.builder()
                .code("PROD-0001")
                .name("Low Stock Item")
                .price(10.0)
                .stock(2)
                .categories(new HashSet<>())
                .build();

        ProductEntity highStock = ProductEntity.builder()
                .code("PROD-0002")
                .name("High Stock Item")
                .price(10.0)
                .stock(20)
                .categories(new HashSet<>())
                .build();

        productRepository.saveAll(List.of(lowStock, highStock));

        // Act
        List<ProductEntity> result = productRepository.findByStockLessThan(threshold);

        // Assert
        assertNotNull(result, "The result list should not be null");
        assertEquals(1, result.size(), "Should find exactly one product with low stock");
        assertEquals("PROD-0001", result.get(0).getCode(), "The code of the filtered product should match");
    }

    /**
     * Verifies that an empty {@link Optional} is returned when searching
     * for a non-existent code.
     */
    @Test
    @DisplayName("Should return an empty Optional when the product code does not exist")
    void findByCodeNotFound() {
        // Act
        Optional<ProductEntity> result = productRepository.findByCode("PROD-9999");

        // Assert
        assertTrue(result.isEmpty(), "Result should be empty for a non-existent product code");
    }
}