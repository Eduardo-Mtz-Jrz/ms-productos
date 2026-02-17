package com.ms_products.ms_products.service;

import com.ms_products.ms_products.entity.ProductEntity;
import com.ms_products.ms_products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    // ADD PRODUCT
    @Override
    public ProductEntity productAdd(@NonNull ProductEntity product) {

        productRepository.findByCode(product.getCode())
                .ifPresent(p -> {
                    throw new RuntimeException("Product code already exists");
                });

        return productRepository.save(product);
    }

    // UPDATE PRODUCT
    @Override
    public ProductEntity productUpdate(Long id, @NonNull ProductEntity product) {

        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setCode(product.getCode());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategory(product.getCategory());

        return productRepository.save(existingProduct);
    }

    // DELETE PRODUCT
    @Override
    public void productDelete(Long id) {

        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(existingProduct);
    }

    // GET PRODUCT BY ID
    @Override
    public ProductEntity productGetById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // GET ALL PRODUCTS
    @Override
    public List<ProductEntity> productGetAll() {
        return productRepository.findAll();
    }
}
