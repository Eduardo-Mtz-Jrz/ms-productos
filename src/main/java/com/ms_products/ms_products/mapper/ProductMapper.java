package com.ms_products.ms_products.mapper;

import com.ms_products.ms_products.dto.ProductRequestDTO;
import com.ms_products.ms_products.dto.ProductResponseDTO;
import com.ms_products.ms_products.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") // Esto permite que Spring lo inyecte como un Bean
public interface ProductMapper {

    // Convierte de Entidad a DTO (para las respuestas)
    ProductResponseDTO toDto(ProductEntity entity);

    // Convierte de DTO a Entidad (para crear nuevos)
    @Mapping(target = "id", ignore = true) // El ID lo genera la DB, lo ignoramos al mapear
    ProductEntity toEntity(ProductRequestDTO dto);

    // Método utilitario para actualizar una entidad existente con datos de un DTO
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ProductRequestDTO dto, @MappingTarget ProductEntity entity);
}