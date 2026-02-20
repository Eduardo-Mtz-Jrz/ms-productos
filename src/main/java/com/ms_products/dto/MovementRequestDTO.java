package com.ms_products.dto;

import com.ms_products.enums.TypeEnum;
import com.ms_products.utils.validEnumTypeEnum.TypeEnumAnnotation;
import jakarta.validation.constraints.NotNull;

public record IdempotenteRequestDTO(@NotNull  Long idEpotrncyKey,
                                    @NotNull Long productId,
                                    @NotNull Double quantity,
                                    @TypeEnumAnnotation(enumClass = TypeEnum.class) TypeEnum type
                                    ) {
}
