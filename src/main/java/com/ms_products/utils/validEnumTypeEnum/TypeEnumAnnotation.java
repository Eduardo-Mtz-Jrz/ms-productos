package com.ms_products.utils.validEnumTypeEnum;

import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.PARAMETER)
public @interface TypeEnumAnnotation {
    Class<? extends Enum<?>> enumClass();
    String message() default "{dto.idemporequest.typeenum1}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
