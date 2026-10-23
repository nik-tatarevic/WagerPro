package com.wagerpro.wagerservice.application.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.wagerpro.wagerservice.application.dto.ItemOddDetailsDto;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateTicketRequestDto(
		@NotNull
		BigDecimal totalStake,
		
		@NotNull
		BigDecimal totalOdds,
		
		@NotNull
		BigDecimal potentialPayout,
		
		@NotNull
		TicketType type,
		
		@NotEmpty
		List<ItemOddDetailsDto> itemsAndOdds
		
) {}
