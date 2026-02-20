package com.ms_products.client;

import com.ms_products.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign actualizado para obtener el perfil completo del usuario.
 */
@FeignClient(
        name = "${services.ms-usuarios.name:ms-usuarios}",
        url = "${services.ms-usuarios.url:http://localhost:8082}",
        fallback = UserClientFallback.class
)
public interface UserClient {

    /**
     * Obtiene los detalles del usuario, incluyendo su rol.
     * @param userId ID del usuario a consultar.
     * @return Objeto con id y role.
     */
    @GetMapping("/api/users/{userId}")
    UserResponseDTO getUserById(@PathVariable("userId") Long userId);
}