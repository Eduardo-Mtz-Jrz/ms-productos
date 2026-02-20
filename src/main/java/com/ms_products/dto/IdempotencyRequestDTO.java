package com.ms_products.controller;

import com.ms_products.dto.IdempotencyRequestDTO;
import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * PASO FINAL: Endpoint para registrar órdenes con Idempotencia.
     * Recibe el DTO con la llave, el producto, cantidad y tipo (IN/OUT).
     */
    @PostMapping("/register")
    public ResponseEntity<Boolean> registerOrder(@RequestBody IdempotencyRequestDTO request) {
        // Llamamos a la lógica de 5 pasos que integramos en el Service
        Boolean result = productService.registerOrder(request);

        // Retornamos 200 OK con el resultado del proceso
        return ResponseEntity.ok(result);
    }

    // --- MÉTODOS CRUD EXISTENTES ---

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO request) {
        return new ResponseEntity<>(productService.save(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO request,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(productService.update(id, request, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}