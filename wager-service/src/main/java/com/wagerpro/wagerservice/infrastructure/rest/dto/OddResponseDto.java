package com.wagerpro.wagerservice.infrastructure.rest.dto;

import java.math.BigDecimal;

public record OddResponseDto(
		Long oddId,
		BigDecimal oddValue,
		Boolean isActive
) {}
