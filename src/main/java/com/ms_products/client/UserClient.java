package com.ms_products.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for inter-service communication with the User Microservice.
 * <p>
 * This client enables the Product service to verify user roles and
 * permissions. It uses externalized configuration for service naming
 * and URLs to ensure environment flexibility.
 * </p>
 *
 * @author Angel Gabriel
 * @version 1.0
 */
@FeignClient(
        name = "${services.ms-usuarios.name:ms-usuarios}",
        url = "${services.ms-usuarios.url:http://localhost:8082}",
        fallback = UserClientFallback.class
)
public interface UserClient {

    /**
     * Checks if a specific user has administrative privileges.
     * <p>
     * This endpoint is crucial for validating write/update operations
     * within the product catalog that are restricted to admin users.
     * </p>
     *
     * @param userId Unique identifier of the user to be checked.
     * @return {@code true} if the user has an ADMIN role, {@code false}
     * otherwise.
     */
    @GetMapping("/api/users/isAdmin/{userId}")
    Boolean isAdmin(@PathVariable("userId") Long userId);
}