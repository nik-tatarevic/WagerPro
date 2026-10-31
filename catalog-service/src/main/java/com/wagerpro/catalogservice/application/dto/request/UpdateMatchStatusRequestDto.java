package com.wagerpro.catalogservice.application.dto.request;

import com.wagerpro.catalogservice.infrastructure.persistence.enums.MatchStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateMatchStatusRequestDto(
		@NotNull(message = "Match status is required")
		Long matchId,
		
		@NotNull(message = "Match status is required")
	    MatchStatus status
) {}
