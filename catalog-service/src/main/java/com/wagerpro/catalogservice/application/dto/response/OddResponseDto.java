package com.wagerpro.catalogservice.application.dto.response;

import java.math.BigDecimal;

public record OddResponseDto(
		Long id,
		Long matchId,
		Long marketDefinitionId,
		BigDecimal oddValue
) {}
