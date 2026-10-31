package com.wagerpro.catalogservice.infrastructure.rest;

public record UserPrincipal(
        Long userId,
        String username,
        Object roles
) {}
