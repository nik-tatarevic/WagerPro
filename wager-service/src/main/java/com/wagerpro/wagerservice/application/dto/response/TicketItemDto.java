package com.wagerpro.wagerservice.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import com.wagerpro.wagerservice.infrastructure.persistence.enums.MarketType;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketItemStatus;

public record TicketItemDto(
		 Long matchId,
		 String sportName,
		 String leagueName,
		 String homeTeam,
		 String awayTeam,
		 LocalDateTime startTime,
		 MarketType marketType,
		 BigDecimal oddValue,
		 TicketItemStatus ticketItemStatus
) {

}
