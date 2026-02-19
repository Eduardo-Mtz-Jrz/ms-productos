package com.ms_products.service.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms_products.controller.ProductController;
import com.ms_products.dto.ProductRequestDTO;
import com.ms_products.exception.ProductNotFoundException;
import com.ms_products.exception.UnauthorizedException;
import com.ms_products.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for Global Exception Handling.
 * <p>
 * This class validates that business exceptions thrown by the service layer
 * are correctly intercepted by the {@link com.ms_products.exception.GlobalExceptionHandler}
 * and translated into proper HTTP responses.
 * </p>
 */
@WebMvcTest(ProductController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Mock of the product service to simulate exception scenarios.
     */
    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case for {@link ProductNotFoundException}.
     * <p> Verifies that a 404 Not Found status and a consistent error message
     * are returned when a product does not exist. </p>
     */
    @Test
    @DisplayName("Should return 404 Not Found when product does not exist")
    void handleProductNotFoundTest() throws Exception {
        Long id = 1L;
        String expectedMessage = "Product with ID " + id + " not found.";

        when(productService.findById(id)).thenThrow(new ProductNotFoundException(id));

        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(expectedMessage))
                .andExpect(jsonPath("$.status").value(404));
    }

    /**
     * Test case for {@link UnauthorizedException}.
     * <p> Verifies that a 403 Forbidden status is returned when an admin-only
     * operation is attempted by an unauthorized user. </p>
     */
    @Test
    @DisplayName("Should return 403 Forbidden when user lacks admin privileges")
    void handleUnauthorizedExceptionTest() throws Exception {
        Long id = 1L;
        Long userId = 99L;
        String errorMessage = "User does not have admin privileges";

        // Valid DTO to bypass @Valid annotation in the controller
        ProductRequestDTO validDto = ProductRequestDTO.builder()
                .code("PROD-1234")
                .name("Test Product")
                .category("Test")
                .price(10.0)
                .stock(5)
                .build();

        when(productService.update(eq(id), any(ProductRequestDTO.class), eq(userId)))
                .thenThrow(new UnauthorizedException(errorMessage));

        mockMvc.perform(put("/api/products/{id}", id)
                        .header("X-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }
}