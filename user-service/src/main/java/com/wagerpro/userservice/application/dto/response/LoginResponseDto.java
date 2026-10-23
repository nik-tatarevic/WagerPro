package com.wagerpro.userservice.application.dto.response;

public record LoginResponseDto(
		String accessToken,
        String tokenType,
        long expiresInSeconds,
        UserResponseDto user	
) {}
