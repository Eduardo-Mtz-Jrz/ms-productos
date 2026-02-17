package com.ms_products.ms_products.controller;

import com.ms_products.ms_products.dto.ProductRequestDTO;
import com.ms_products.ms_products.dto.ProductResponseDTO;
import com.ms_products.ms_products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter; // Agregado para Swagger

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Controller for managing product catalog")
public class ProductController {

    private final ProductService productService;

    // GET ALL
    @Operation(summary = "Get all products")
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    // GET by ID
    @Operation(summary = "Get product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    // POST
    @Operation(summary = "Create a new product")
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO request) {
        ProductResponseDTO newProduct = productService.save(request);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    // PUT - MODIFICADO PARA EL LABORATORIO
    @Operation(summary = "Update an existing product (Requires Admin validation)")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO request,
            @Parameter(description = "ID of the user making the request")
            @RequestHeader("X-User-Id") Long userId) { // <-- Recibimos el userId desde el Header

        // Ahora pasamos el id, el request y el userId al servicio
        return ResponseEntity.ok(productService.update(id, request, userId));
    }

    // DELETE
    @Operation(summary = "Delete a product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}