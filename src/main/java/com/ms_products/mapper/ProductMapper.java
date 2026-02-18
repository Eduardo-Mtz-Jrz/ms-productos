package com.ms_products.mapper;

import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for converting between Product entities and DTOs.
 * <p>
 * This interface uses MapStruct to generate the implementation code.
 * The {@code unmappedTargetPolicy = ReportingPolicy.IGNORE} is
 * applied to prevent
 * SonarQube warnings and compilation errors regarding missing
 * field mappings
 * between different data structures.
 * </p>
 *
 * @author Angel Gabriel
 * @version 1.0
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    /**
     * Transforms a {@link ProductEntity} into a {@link ProductResponseDTO}.
     *
     * @param entity The database entity to be converted.
     * @return A DTO containing the product's response data.
     */
    ProductResponseDTO toDto(ProductEntity entity);

    /**
     * Transforms a {@link ProductRequestDTO} into a new
     * {@link ProductEntity}.
     * <p>
     * Note: The {@code id} field is ignored during mapping
     * to ensure that
     * the JPA provider handles the generation of the primary key.
     * </p>
     *
     * @param dto The DTO containing creation data.
     * @return A new entity ready for persistence.
     */
    @Mapping(target = "id", ignore = true)
    ProductEntity toEntity(ProductRequestDTO dto);

    /**
     * Updates an existing {@link ProductEntity} instance with
     * data from a {@link ProductRequestDTO}.
     * <p>
     * This method performs an in-place update of the target entity.
     * The {@code id} field is ignored to prevent accidental modification
     * of the entity's identity.
     * </p>
     *
     * @param dto    The source DTO containing the new data.
     * @param entity The target entity to be updated.
     */
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ProductRequestDTO dto, @MappingTarget ProductEntity entity);
}