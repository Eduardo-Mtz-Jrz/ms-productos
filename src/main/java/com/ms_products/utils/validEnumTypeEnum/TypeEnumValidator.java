package com.ms_products.utils.validEnumTypeEnum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TypeEnumValidator implements ConstraintValidator<TypeEnumAnnotation, String> {
    @Override
    public void initialize(TypeEnumAnnotation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isBlank()) return true;
        return false;
    }
}
