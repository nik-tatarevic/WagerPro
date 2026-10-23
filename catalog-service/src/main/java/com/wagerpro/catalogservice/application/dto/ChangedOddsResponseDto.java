package com.wagerpro.catalogservice.application.dto;

import java.math.BigDecimal;

public record ChangedOddsResponseDto(
		Long oddId,
		BigDecimal oddValue,
		Boolean isActive
		) {

}
