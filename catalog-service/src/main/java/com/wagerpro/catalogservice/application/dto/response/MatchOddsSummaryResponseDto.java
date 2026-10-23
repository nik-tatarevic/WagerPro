package com.wagerpro.catalogservice.application.dto.response;

import java.math.BigDecimal;

import com.wagerpro.catalogservice.infrastructure.persistence.enums.MarketDefinitionType;

public record MatchOddsSummaryResponseDto(
		Long id, 
		String type,
		BigDecimal oddValue
) {}
