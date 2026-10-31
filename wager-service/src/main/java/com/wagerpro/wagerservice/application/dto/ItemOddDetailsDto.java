package com.wagerpro.wagerservice.application.dto;

import java.math.BigDecimal;

import org.jspecify.annotations.Nullable;

import com.wagerpro.wagerservice.infrastructure.persistence.enums.MarketType;

public record ItemOddDetailsDto(
		@Nullable
		Long ticketId,
		Long matchId,
		Long oddId,
		BigDecimal oddValue,
		MarketType marketType
) {

	public ItemOddDetailsDto setTicketId(Long savedTicketId) {
		return new ItemOddDetailsDto(savedTicketId, matchId, oddId, oddValue, marketType);
	}
	
}
