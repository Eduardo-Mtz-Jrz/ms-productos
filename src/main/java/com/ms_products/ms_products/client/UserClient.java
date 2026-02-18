package com.ms_products.ms_products.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// CAMBIAR PUERTO PARA MS_USUARIOS (o el que use tu micro de usuarios).
@FeignClient(name = "ms-usuarios", url = "http://localhost:8082")
public interface UserClient {

    // Ajustamos la ruta para que coincida con "isAdmin/5"
    @GetMapping("/api/users/isAdmin/{userId}")
    Boolean isAdmin(@PathVariable("userId") Long userId);
}