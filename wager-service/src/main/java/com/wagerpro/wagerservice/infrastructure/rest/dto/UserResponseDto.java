package com.wagerpro.wagerservice.infrastructure.rest.dto;

public record UserResponseDto(
		Long id,
        String username,
        String email,
        String role,
        boolean enabled
) {}
