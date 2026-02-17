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

    // AGREGAR
    @Override
    public ProductEntity agregar(@NonNull ProductEntity product) {

        productRepository.findByCode(product.getCode())
                .ifPresent(p -> {
                    throw new RuntimeException("El código ya existe");
                });

        return productRepository.save(product);
    }

    // MODIFICAR
    @Override
    public ProductEntity modificar(Long id, @NonNull ProductEntity product) {

        ProductEntity existente = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        existente.setName(product.getName());
        existente.setCode(product.getCode());
        existente.setPrice(product.getPrice());
        existente.setStock(product.getStock());
        existente.setCategory(product.getCategory());

        return productRepository.save(existente);
    }

    // ELIMINAR
    @Override
    public void eliminar(Long id) {

        ProductEntity existente = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productRepository.delete(existente);
    }

    // CONSULTAR POR ID
    @Override
    public ProductEntity obtenerPorId(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // LISTAR TODOS
    @Override
    public List<ProductEntity> listar() {
        return productRepository.findAll();
    }
}
