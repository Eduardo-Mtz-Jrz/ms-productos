package com.ma_products.ms_products.controller;

import com.ma_products.ms_products.dto.request.ProductoRequestDTO;
import com.ma_products.ms_products.dto.response.ProductoResponseDTO;
import com.ma_products.ms_products.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "Controlador para la gestión del catálogo de productos")
public class ProductController {

    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Obtener productos")
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @Operation(summary = "Obtener producto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @Operation(summary = "Agregar nuevo producto")
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO request) {
        ProductoResponseDTO nuevoProducto = productoService.guardar(request);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un producto ")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar un producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

