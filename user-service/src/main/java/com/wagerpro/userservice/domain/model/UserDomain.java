package com.wagerpro.userservice.domain.model;

import java.time.LocalDateTime;

public record UserDomain(
        Long id,
        String username,
        String email,
        String hashedPassword,
        Role role,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
