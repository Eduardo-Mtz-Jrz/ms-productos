package com.ms_products.service.mapper;

import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.entity.ProductEntity;
import com.ms_products.mapper.ProductMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Test
    @DisplayName("Should map Request DTO to Entity")
    void toEntityTest() {
        ProductRequestDTO dto = ProductRequestDTO.builder()
                .code("P-100")
                .name("Test")
                .price(10.0)
                .build();

        ProductEntity entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getCode(), entity.getCode());
        assertNull(entity.getId(), "ID should be null as it is ignored in mapping");
    }

    @Test
    @DisplayName("Should map Entity to Response DTO")
    void toDtoTest() {
        ProductEntity entity = ProductEntity.builder()
                .id(1L)
                .code("P-100")
                .name("Test")
                .build();

        ProductResponseDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getName(), dto.getName());
    }

    @Test
    @DisplayName("Should update existing Entity from DTO")
    void updateEntityFromDtoTest() {
        ProductEntity entity = ProductEntity.builder().id(1L).name("Old Name").build();
        ProductRequestDTO dto = ProductRequestDTO.builder().name("New Name").build();

        mapper.updateEntityFromDto(dto, entity);

        assertEquals("New Name", entity.getName());
        assertEquals(1L, entity.getId(), "ID should remain unchanged");
    }

    @Test
    @DisplayName("Should handle null mappings gracefully")
    void nullMappingTest() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.toEntity(null));
        // updateEntityFromDto(null, entity) no debería lanzar excepción
        ProductEntity entity = new ProductEntity();
        mapper.updateEntityFromDto(null, entity);
        assertNotNull(entity);
    }
}