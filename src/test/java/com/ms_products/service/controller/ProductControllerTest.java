package com.ms_products.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms_products.controller.ProductController;
import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.dto.ProductResponseDTO;
import com.ms_products.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequestDTO validRequest;
    private ProductResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        validRequest = ProductRequestDTO.builder()
                .code("PROD-0001")
                .name("Standard Product")
                .category("General")
                .price(100.0)
                .stock(10)
                .build();

        responseDTO = ProductResponseDTO.builder()
                .id(1L)
                .code("PROD-0001")
                .name("Standard Product")
                .build();
    }

    // --- NUEVOS TESTS PARA COBERTURA GET (LOS QUE ESTABAN EN ROJO) ---

    @Test
    @DisplayName("GET /api/products -> 200 OK")
    void getAllProducts_Success() throws Exception {
        when(productService.findAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("PROD-0001"));
    }

    @Test
    @DisplayName("GET /api/products/{id} -> 200 OK")
    void getProductById_Success() throws Exception {
        Long productId = 1L;
        when(productService.findById(productId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Standard Product"));
    }

    // --- TESTS EXISTENTES (POST, PUT, DELETE) ---

    @Test
    @DisplayName("POST /api/products -> 201 Created")
    void createProductSuccess() throws Exception {
        when(productService.save(any(ProductRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("POST /api/products -> 400 Bad Request")
    void createProductValidationFail() throws Exception {
        ProductRequestDTO invalidRequest = new ProductRequestDTO(); // Campos nulos violan @NotBlank

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/products/{id} -> 200 OK")
    void updateProductSuccess() throws Exception {
        Long productId = 1L;
        Long userId = 99L;

        when(productService.update(eq(productId), any(ProductRequestDTO.class), eq(userId)))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/products/{id}", productId)
                        .header("X-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/products/{id} -> 204 No Content")
    void deleteProductTest() throws Exception {
        mockMvc.perform(delete("/api/products/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}