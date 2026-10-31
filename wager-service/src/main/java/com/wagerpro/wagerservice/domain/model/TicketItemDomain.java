package com.wagerpro.wagerservice.domain.model;

import java.math.BigDecimal;

import com.wagerpro.wagerservice.domain.exception.TicketStatusException;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.MarketType;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketItemStatus;

public record TicketItemDomain(
		Long id, 
		Long ticketId, 
		Long matchId, 
		Long oddId, 
		BigDecimal oddValue,
		MarketType marketType, 
		TicketItemStatus status) {
	
	public TicketItemDomain marAsWon() {
		ensurePending();
		return passStatus(TicketItemStatus.WON);
	}

	public TicketItemDomain markAsLost() {
		ensurePending();
		return passStatus(TicketItemStatus.LOST);
	}

	public TicketItemDomain markAsPending() {
		ensurePending();
		return passStatus(TicketItemStatus.PENDING);
	}

	public TicketItemDomain markAsCancelled() {
		ensurePending();
		return passStatus(TicketItemStatus.CANCELLED);
	}

	private void ensurePending() {
		if (status != TicketItemStatus.PENDING) {
			throw new TicketStatusException("Only PENDING ticket can change status.");
		}
	}

	private TicketItemDomain passStatus(TicketItemStatus status) {
		if (status == TicketItemStatus.CANCELLED) {
			return new TicketItemDomain(id, ticketId, matchId, oddId, BigDecimal.ONE, marketType, status);
		}

		return new TicketItemDomain(id, ticketId, matchId, oddId, oddValue, marketType, status);
	}

}
