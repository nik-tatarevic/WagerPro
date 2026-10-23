package com.wagerpro.wagerservice.infrastructure.rest;

public record UserPrincipal(
		Long userId,
		String username,
		Object roles
		) {

}
