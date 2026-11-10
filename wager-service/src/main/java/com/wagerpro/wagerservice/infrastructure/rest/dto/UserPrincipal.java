package com.wagerpro.wagerservice.infrastructure.rest.dto;

public record UserPrincipal(
		Long userId,
		String username,
		Object roles
) {}
