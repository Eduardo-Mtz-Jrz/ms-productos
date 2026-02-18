package com.ms_products.ms_products.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ProductEntityTest {

    private ProductEntity productEntity;


    @ParameterizedTest
    @ValueSource(strings = {"1","a"})
    void idTestValid(String values){
        //assertDoesNotThrow(() -> {
            this.productEntity.setId(Long.valueOf(values));
        //});
    }

}