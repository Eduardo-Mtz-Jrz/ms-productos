package com.ms_products.ms_products.controller;

import com.ms_products.ms_products.dto.ProductRequestDTO;
import com.ms_products.ms_products.dto.ProductResponseDTO;
import com.ms_products.ms_products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Product management.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints for product catalog management")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Retrieve all products")
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @Operation(summary = "Find product by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(summary = "Create a new product record")
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO request) {
        return new ResponseEntity<>(productService.save(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing product with admin validation")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "403", description = "User does not have admin rights"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO request,
            @Parameter(description = "Authenticated User ID", required = true)
            @RequestHeader(value = "X-User-Id") Long userId) {

        return ResponseEntity.ok(productService.update(id, request, userId));
    }

    @Operation(summary = "Remove a product from the catalog")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}