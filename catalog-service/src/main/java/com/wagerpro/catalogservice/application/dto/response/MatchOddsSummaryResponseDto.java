package com.wagerpro.catalogservice.application.dto.response;

import java.math.BigDecimal;

public record MatchOddsSummaryResponseDto(
		Long id,
		Long marketDefinitionId,
		BigDecimal oddValue
) {}
