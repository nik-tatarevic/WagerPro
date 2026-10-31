package com.wagerpro.wagerservice.infrastructure.rest.dto;

import java.math.BigDecimal;

public record ChangedOddsResponseDto(
		Long oddId,
		BigDecimal oddValue,
		Boolean isActive
) {}
