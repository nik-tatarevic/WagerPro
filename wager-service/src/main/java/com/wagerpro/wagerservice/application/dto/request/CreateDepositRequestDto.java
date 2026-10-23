package com.wagerpro.wagerservice.application.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateDepositRequestDto(
		@NotNull
		Long userId, 
		
		@NotNull
		@Positive
		BigDecimal amount		
) {

}
