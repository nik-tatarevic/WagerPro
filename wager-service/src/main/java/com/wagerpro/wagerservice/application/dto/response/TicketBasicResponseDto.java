package com.wagerpro.wagerservice.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketStatus;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketType;

public record TicketBasicResponseDto(
		Long id,
		BigDecimal totalStake,
		BigDecimal potentialPayout,
		TicketType type,
		LocalDateTime createdAt,
		TicketStatus status
) {}
