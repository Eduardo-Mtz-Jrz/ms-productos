package com.ms_products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for User profile information.
 * <p>
 * This DTO is used to receive essential user data from the User Microservice,
 * specifically for role-based access control (RBAC) validation within
 * the product service.
 * </p>
 *
 * @author Angel Gabriel
 * @version 1.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    /**
     * Unique identifier of the user.
     */
    private Long id;

    /**
     * Security role assigned to the user (e.g., "ADMIN", "USER").
     * Used to authorize sensitive operations like product updates.
     */
    private String role;
}