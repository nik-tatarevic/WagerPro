package com.wagerpro.userservice.infrastructure.security.dto;

import java.time.Instant;

public record TokenResult(
	    String accessToken,
	    String jti,
	    Instant expiresAt
) {}
