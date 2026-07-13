package com.example.bankcards.security;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.ResourceNotFoundException;
import com.example.bankcards.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Предоставляет ID текущего аутентифицированного пользователя,
 * извлекая аутентификацию из {@link SecurityContextHolder}.
 */
@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Возвращает ID текущего аутентифицированного пользователя.
     *
     * @throws ResourceNotFoundException если аутентифицированный пользователь не найден
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User not authenticated");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
