package com.ms_products.service.repository;



import com.ms_products.entity.ProductEntity;
import com.ms_products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ProductRepository}.
 * Uses an in-memory database to validate custom query methods.
 */
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private ProductEntity sampleProduct;

    @BeforeEach
    void setUp() {
        // Essential: Add the 'category' field to prevent ConstraintViolationException
        sampleProduct = ProductEntity.builder()
                .name("Wireless Mouse")
                .code("PROD-1010")
                .category("Peripherals") // Added to fix the 'Category cannot be empty' error
                .price(25.99)
                .stock(50)
                .build();
    }

    @Test
    @DisplayName("Should find a product by its unique business code")
    void findByCodeTest() {
        // GIVEN
        productRepository.save(sampleProduct);

        // WHEN
        Optional<ProductEntity> found = productRepository.findByCode("PROD-1010");

        // THEN
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Wireless Mouse");
    }

    @Test
    @DisplayName("Should return products below the stock threshold")
    void findByStockLessThanTest() {
        // GIVEN
        ProductEntity lowStockItem = ProductEntity.builder()
                .name("Battery")
                .code("PROD-2020")
                .category("Accessories") // Ensure category is present here too
                .price(5.0)
                .stock(3)
                .build();

        productRepository.saveAll(List.of(sampleProduct, lowStockItem));

        // WHEN
        List<ProductEntity> result = productRepository.findByStockLessThan(10);

        // THEN
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Battery");
    }
}