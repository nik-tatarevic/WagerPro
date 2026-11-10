package com.wagerpro.catalogservice.application.dto.response;

import java.math.BigDecimal;

public record OddSummaryResponseDto(
		Long id,
		Long marketDefinitionId,
		BigDecimal oddValue
) {}
