package com.wagerpro.wagerservice.application.dto.response;

import java.math.BigDecimal;

import com.wagerpro.wagerservice.infrastructure.persistence.enums.WalletStatus;

public record WalletResponseDto(
		Long id, 
		Long userId, 
		BigDecimal balance, 
		String currency, 
		WalletStatus status
		) {

}
