package com.ms_products.ms_products.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client to communicate with the User Microservice.
 * Configuration is handled via application.properties.
 */
@FeignClient(
        name = "${services.ms-usuarios.name:ms-usuarios}",
        url = "${services.ms-usuarios.url:http://localhost:8082}",
        fallback = UserClientFallback.class
)
public interface UserClient {

    @GetMapping("/api/users/isAdmin/{userId}")
    Boolean isAdmin(@PathVariable("userId") Long userId);
}