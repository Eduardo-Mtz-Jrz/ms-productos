package com.ms_products.ms_products.service;

import com.ms_products.ms_products.dto.ProductRequestDTO;
import com.ms_products.ms_products.dto.ProductResponseDTO;
import com.ms_products.ms_products.entity.ProductEntity;
import com.ms_products.ms_products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // CREATE
    @Override
    public ProductResponseDTO save(ProductRequestDTO request) {

        productRepository.findByCode(request.getCode())
                .ifPresent(p -> {
                    throw new RuntimeException("Product code already exists");
                });

        ProductEntity product = mapToEntity(request);
        return mapToDTO(productRepository.save(product));
    }

    // UPDATE
    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO request) {

        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(request.getName());
        existingProduct.setCode(request.getCode());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setStock(request.getStock());
        existingProduct.setCategory(request.getCategory());

        return mapToDTO(productRepository.save(existingProduct));
    }

    // DELETE
    @Override
    public void delete(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    // GET BY ID
    @Override
    public ProductResponseDTO findById(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDTO(product);
    }

    // GET ALL
    @Override
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // MAPPERS
    private ProductEntity mapToEntity(ProductRequestDTO dto) {
        ProductEntity product = new ProductEntity();
        product.setName(dto.getName());
        product.setCode(dto.getCode());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(dto.getCategory());
        return product;
    }

    private ProductResponseDTO mapToDTO(ProductEntity entity) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        dto.setCategory(entity.getCategory());
        return dto;
    }
}
