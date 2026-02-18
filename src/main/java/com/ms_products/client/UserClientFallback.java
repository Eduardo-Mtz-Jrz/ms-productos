package com.ms_products.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Fallback implementation for {@link UserClient} using the Circuit
 * Breaker pattern.
 * <p>
 * This class provides graceful degradation when the User Microservice
 * is unavailable. It implements a "fail-safe" security approach by
 * denying administrative privileges by default during communication
 * failures.
 * </p>
 *
 * @author Angel Gabriel
 * @version 1.0
 */
@Slf4j
@Component
public class UserClientFallback implements UserClient {

    /**
     * Provides a default security response when the user service call
     * fails.
     * <p>
     * For security reasons, this method returns {@code false} if the
     * circuit is open or the remote service is unreachable, ensuring
     * that unauthorized users cannot gain administrative access due
     * to system errors.
     * </p>
     *
     * @param userId Unique identifier of the user that was being
     * validated.
     * @return Always {@code false} as a safe default during failures.
     */
    @Override
    public Boolean isAdmin(Long userId) {
        log.error("Error calling ms-usuarios for userId: {}. " +
                "Defaulting to false.", userId);
        return false;
    }
}