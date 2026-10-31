package com.wagerpro.catalogservice.application.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record CreateOrUpdateOddRequestDto(
		@NotNull
		Long matchId,
		
		@NotNull
		Long marketDefinitionId,
		
		@NotNull
		BigDecimal oddValue,
		
		@NotNull
		Boolean isActive
) {}
