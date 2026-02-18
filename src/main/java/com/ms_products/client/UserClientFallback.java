package com.ms_products.ms_products.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserClientFallback implements UserClient {
    @Override
    public Boolean isAdmin(Long userId) {
        log.error("Error calling ms-usuarios for userId: {}. Defaulting to false.", userId);
        return false; // Por seguridad, si falla la comunicación, no es admin
    }
