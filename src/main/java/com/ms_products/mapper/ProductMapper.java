package com.ms_products.mapper;

import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for data transformation between Product entities and DTOs.
 * <p>
 * This component uses MapStruct to generate high-performance mapping code
 * at compile-time. It handles the conversion of the 'category' field and
 * ensures data integrity by protecting sensitive fields like entity IDs.
 * </p>
 * +
 * @version 1.1
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    /**
     * Converts a {@link ProductEntity} into a {@link ProductResponseDTO}.
     * <p>
     * Automatically maps common fields. The 'category' string from the entity
     * is directly mapped to the response DTO.
     * </p>
     *
     * @param entity The database entity to be converted.
     * @return A DTO containing the product details for the client.
     */
    ProductResponseDTO toDto(ProductEntity entity);

    /**
     * Transforms a {@link ProductRequestDTO} into a new {@link ProductEntity}.
     * <p>
     * Security constraints:
     * <ul>
     * <li>The 'id' field is ignored to allow JPA's auto-generation strategy.</li>
     * <li>The 'categories' Set is ignored to avoid conflicts with the simple
     * String-based category classification.</li>
     * </ul>
     * </p>
     *
     * @param dto The source DTO containing product creation data.
     * @return A new entity instance ready for persistence.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    ProductEntity toEntity(ProductRequestDTO dto);

    /**
     * Performs an in-place update of an existing {@link ProductEntity}
     * using data from a {@link ProductRequestDTO}.
     * <p>
     * This method is used for PATCH/PUT operations where the entity's
     * identity must be preserved while updating its attributes.
     * </p>
     *
     * @param dto    The source DTO with updated information.
     * @param entity The target entity to be modified.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    void updateEntityFromDto(ProductRequestDTO dto, @MappingTarget ProductEntity entity);
}