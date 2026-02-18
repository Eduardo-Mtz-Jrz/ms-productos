package com.ms_products.ms_products.mapper;

import com.ms_products.ms_products.dto.ProductRequestDTO;
import com.ms_products.ms_products.dto.ProductResponseDTO;
import com.ms_products.ms_products.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for Product transformations.
 * unmappedTargetPolicy = ReportingPolicy.IGNORE ensures SonarQube doesn't flag
 * missing fields between DTO and Entity.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductResponseDTO toDto(ProductEntity entity);

    @Mapping(target = "id", ignore = true)
    ProductEntity toEntity(ProductRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ProductRequestDTO dto, @MappingTarget ProductEntity entity);
}