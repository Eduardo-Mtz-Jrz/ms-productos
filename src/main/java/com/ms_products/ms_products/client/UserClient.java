package com.ms_products.ms_products.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// El name debe ser el nombre del microservicio de usuarios en Eureka o su URL
@FeignClient(name = "ms-usuarios", url = "http://localhost:8081")
public interface UserClient {

    @GetMapping("/api/users/check-admin/{userId}")
    Boolean isAdmin(@PathVariable("userId") Long userId);
}