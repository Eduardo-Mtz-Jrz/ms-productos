package com.ms_products.ms_products.service;

import com.ms_products.ms_products.entity.ProductEntity;

import java.util.List;

public interface ProductService {

    // ADD PRODUCT
    ProductEntity productAdd(ProductEntity product);

    // UPDATE PRODUCT
    ProductEntity productUpdate(Long id, ProductEntity product);

    // DELETE PRODUCT
    void productDelete(Long id);

    // GET PRODUCT BY ID
    ProductEntity productGetById(Long id);

    // GET ALL PRODUCTS
    List<ProductEntity> productGetAll();
}
