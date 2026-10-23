package com.wagerpro.wagerservice.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.wagerpro.wagerservice.domain.exception.TicketStatusException;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketItemStatus;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketStatus;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketType;

import jakarta.validation.constraints.NotNull;

public record TicketDomain(
		Long id, 
		Long userId, 
		TicketType type, 
		BigDecimal totalStake, 
		BigDecimal totalOdds,
		BigDecimal potentialPayout, 
		TicketStatus status, 
		List<TicketItemDomain> items, 
		LocalDateTime createdAt,
		Long version
) {
	
	public TicketDomain setUserId(Long userIdFromContext) {
		return new TicketDomain(id, userIdFromContext, type, totalStake, 
				totalOdds, potentialPayout, status, items, createdAt, version);
	}
	
	public TicketDomain markAsWon() {
		ensurePending();
		return withStatus(TicketStatus.WON);
	}

	public TicketDomain markAsLost() {
		ensurePending();
		return withStatus(TicketStatus.LOST);
	}

	public TicketDomain markAsCancelled() {
		ensurePending();
		return withStatus(TicketStatus.CANCELLED);
	}

	private void ensurePending() {
		if (status != TicketStatus.PENDING) {
			throw new TicketStatusException("Only PENDING ticket can change status.");
		}
	}

	private TicketDomain withStatus(TicketStatus status) {
		return new TicketDomain(id, userId, type, totalStake, 
				totalOdds, potentialPayout, status, items, createdAt, version);
	}

	public TicketDomain calculateStatus() {
		boolean hasPending = items().stream().anyMatch(x -> x.status() == TicketItemStatus.PENDING);
		
		if (hasPending) {
			return this;
		}
		
		long totalItems = items().size();
		long countWon = items().stream().filter(x -> x.status() == TicketItemStatus.WON).count();
		long countLost = items().stream().filter(x -> x.status() == TicketItemStatus.LOST).count();
		long countCancelled = items().stream().filter(x -> x.status() == TicketItemStatus.CANCELLED).count();
		
		boolean allWonOrCancelled = countLost == 0 && (countWon + countCancelled) == totalItems;
		// cashback for all tickets with one miss and totalOdd 25+
		// TODO
//		PromotionStrategy
//		CashbackPromotionStrategy
//		NoPromotionStrategy
//		PromotionService
//		i onda
//		PromotionService
//		↓
//		returns
//		Optional<Promotion>
		
		boolean cashbackPromotion = countLost == 1 && (countWon + countCancelled) == totalItems - 1 
				&& (totalOdds().compareTo(new BigDecimal(25)) >= 0);
	
		if (allWonOrCancelled) {
			return markAsWon();
		} else if (cashbackPromotion) {
			return new TicketDomain(id, userId, type, totalStake, 
					totalOdds, totalStake, TicketStatus.WON, items, createdAt, version);
		} else {
			return markAsLost();
		}
	}

	public TicketDomain updatePotentialPayout() {
		BigDecimal totalOdds = items().stream()
				.map(x -> x.oddValue())
				.reduce(BigDecimal.ONE, BigDecimal::multiply);
		
		return new TicketDomain(id, userId, type, totalStake, 
								 totalOdds, totalOdds.multiply(totalStake), 
								 status, items, createdAt, version);
	}
	
}
