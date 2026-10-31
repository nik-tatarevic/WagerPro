package com.wagerpro.wagerservice.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketType;

public record TicketDetailsResponseDto(
		Long ticketId,
		TicketType type,
		LocalDateTime createdAt,
		BigDecimal totalStake,
		BigDecimal totalOdds,
		BigDecimal potentialPayout,
		List<TicketItemDto> items
) {}
