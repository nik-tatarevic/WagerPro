package com.wagerpro.catalogservice.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record OddsValueRequestDto(
		@NotNull
		Long oddId,
		@NotNull
		BigDecimal oddValue
		) {

}
