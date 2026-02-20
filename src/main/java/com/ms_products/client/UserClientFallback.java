package com.ms_products.client;

import com.ms_products.dto.UserResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Fallback implementation for {@link UserClient} using the Circuit Breaker pattern.
 * <p>
 * This class provides graceful degradation when the User Microservice is unavailable.
 * It implements a "fail-safe" security approach by returning a non-privileged role
 * during communication failures, preventing accidental administrative access.
 * </p>
 *
 * @author Angel Gabriel
 * @version 1.1
 */
@Slf4j
@Component
public class UserClientFallback implements UserClient {

    /**
     * Provides a default security response when the user service call fails.
     * <p>
     * Instead of returning {@code null}, this method returns a {@link UserResponseDTO}
     * with a generic "UNKNOWN" role. This ensures that the downstream authorization
     * logic (e.g., in ProductService) fails closed, denying admin permissions
     * while avoiding {@code NullPointerException}.
     * </p>
     *
     * @param userId Unique identifier of the user that was being validated.
     * @return A {@link UserResponseDTO} containing the userId and a restricted role.
     */
    @Override
    public UserResponseDTO getUserById(Long userId) {
        log.error("Circuit Breaker: Error calling ms-usuarios for userId: {}. " +
                "Applying fail-safe: Role set to UNKNOWN.", userId);

        // Returns a safe object with a non-admin role to maintain system stability.
        return new UserResponseDTO(userId, "UNKNOWN");
    }
}