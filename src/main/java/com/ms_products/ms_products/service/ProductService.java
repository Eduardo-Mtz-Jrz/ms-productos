package com.ms_products.ms_products.service;

import com.ms_products.ms_products.dto.ProductRequestDTO;
import com.ms_products.ms_products.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO save(ProductRequestDTO request);

    // Se agrega Long userId para la validación con Feign requerida en el laboratorio
    ProductResponseDTO update(Long id, ProductRequestDTO request, Long userId);

    void delete(Long id);
    ProductResponseDTO findById(Long id);
    List<ProductResponseDTO> findAll();
}