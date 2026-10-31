package com.wagerpro.userservice.application.dto.response;

import java.time.LocalDateTime;

public record UserResponseDto(
		Long id,
        String username,
        String email,
        String role,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
