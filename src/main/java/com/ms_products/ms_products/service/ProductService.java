package com.ms_products.ms_products.service;

import com.ms_products.ms_products.dto.ProductRequestDTO;
import com.ms_products.ms_products.dto.ProductResponseDTO;
import com.ms_products.ms_products.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    ProductResponseDTO save(ProductRequestDTO request);
    ProductResponseDTO update(Long id, ProductRequestDTO request);
    void delete(Long id);
    ProductResponseDTO findById(Long id);
    List<ProductResponseDTO> findAll();
}

