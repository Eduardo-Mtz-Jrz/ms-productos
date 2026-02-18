package com.ms_products.mapper;

import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.entity.ProductEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-18T16:44:05-0600",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.18 (Microsoft)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponseDTO toDto(ProductEntity entity) {
        if ( entity == null ) {
            return null;
        }

        ProductResponseDTO.ProductResponseDTOBuilder productResponseDTO = ProductResponseDTO.builder();

        productResponseDTO.id( entity.getId() );
        productResponseDTO.name( entity.getName() );
        productResponseDTO.code( entity.getCode() );
        productResponseDTO.price( entity.getPrice() );
        productResponseDTO.stock( entity.getStock() );
        productResponseDTO.category( entity.getCategory() );

        return productResponseDTO.build();
    }

    @Override
    public ProductEntity toEntity(ProductRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProductEntity.ProductEntityBuilder productEntity = ProductEntity.builder();

        productEntity.name( dto.getName() );
        productEntity.code( dto.getCode() );
        productEntity.price( dto.getPrice() );
        productEntity.stock( dto.getStock() );
        productEntity.category( dto.getCategory() );

        return productEntity.build();
    }

    @Override
    public void updateEntityFromDto(ProductRequestDTO dto, ProductEntity entity) {
        if ( dto == null ) {
            return;
        }

        entity.setName( dto.getName() );
        entity.setCode( dto.getCode() );
        entity.setPrice( dto.getPrice() );
        entity.setStock( dto.getStock() );
        entity.setCategory( dto.getCategory() );
    }
}
