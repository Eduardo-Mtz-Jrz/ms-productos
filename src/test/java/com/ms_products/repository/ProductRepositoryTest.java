package com.ms_products.ms_products.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void byCodeTest(){
        this.repository.findByCode("PROD-1234");
    }

}