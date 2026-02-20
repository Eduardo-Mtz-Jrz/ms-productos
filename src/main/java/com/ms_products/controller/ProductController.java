package com.ms_products.controller;

import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Product Management.
 * <p>
 * Handles HTTP requests for the product catalog, including inventory
 * checks, administrative updates, and existence verification.
 * </p>
 * * @author Angel Gabriel
 * @version 1.2
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "Endpoints for managing the product catalog and stock levels")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Retrieve all products", description = "Returns a complete list of products available in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved product list")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @Operation(summary = "Find product by ID", description = "Returns a single product based on its unique database identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(description = "ID of the product to be retrieved") @PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    /**
     * Specialized endpoint to check product existence without throwing 404 errors.
     * Useful for frontend validation or simple verification tasks.
     */
    @Operation(summary = "Verify product existence", description = "Checks if a product exists. Returns 1 for true and 0 for false.")
    @ApiResponse(responseCode = "200", description = "Verification successful")
    @GetMapping(value = "/exists/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> checkProductExistence(
            @Parameter(description = "ID of the product to verify") @PathVariable Long id) {
        // Convert Boolean to 1 (true) or 0 (false)
        return ResponseEntity.ok(productService.existsById(id) ? 1 : 0);
    }

    @Operation(summary = "Create a new product", description = "Persists a new product record. Validates unique code constraints.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Product code already exists")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO request) {
        return new ResponseEntity<>(productService.save(request), HttpStatus.CREATED);
    }

    @Operation(summary = "Update product details", description = "Updates an existing product. Access restricted to admin users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(description = "ID of the product to update") @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO request,
            @Parameter(description = "Internal administrator ID") @RequestHeader(value = "X-User-Id") Long userId) {
        return ResponseEntity.ok(productService.update(id, request, userId));
    }

    @Operation(summary = "Delete a product", description = "Removes a product record from the database by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to delete") @PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}