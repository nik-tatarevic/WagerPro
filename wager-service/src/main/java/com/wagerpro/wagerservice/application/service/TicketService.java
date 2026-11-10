package com.wagerpro.wagerservice.application.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagerpro.wagerservice.application.dto.request.CreateTicketRequestDto;
import com.wagerpro.wagerservice.application.dto.response.MatchDetailsDto;
import com.wagerpro.wagerservice.application.dto.response.TicketBasicResponseDto;
import com.wagerpro.wagerservice.application.dto.response.TicketDetailsResponseDto;
import com.wagerpro.wagerservice.application.dto.response.TicketItemDto;
import com.wagerpro.wagerservice.application.mapper.TicketApiMapper;
import com.wagerpro.wagerservice.application.usecase.CreateTicketUseCase;
import com.wagerpro.wagerservice.domain.exception.OddsChangedException;
import com.wagerpro.wagerservice.domain.model.TicketDomain;
import com.wagerpro.wagerservice.domain.model.TicketItemDomain;
import com.wagerpro.wagerservice.domain.repository.TicketDomainRepository;
import com.wagerpro.wagerservice.domain.repository.TicketItemDomainRepository;
import com.wagerpro.wagerservice.domain.strategy.MarketEvaluatorStrategy;
import com.wagerpro.wagerservice.infrastructure.kafka.event.MatchStatusChangedEvent;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketItemStatus;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketUpdateMode;
import com.wagerpro.wagerservice.infrastructure.rest.CatalogServiceClient;
import com.wagerpro.wagerservice.infrastructure.rest.dto.OddResponseDto;
import com.wagerpro.wagerservice.infrastructure.security.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

	private final TicketDomainRepository ticketDomainRepository;
	private final TicketItemDomainRepository ticketItemDomainRepository;
	private final TicketSettlementService ticketSettlementService;
	private final MarketEvaluatorService marketEvaluatorService;
	private final TicketApiMapper mapper;
	private final SecurityUtils securityUtils;
	private final CreateTicketUseCase createTicketUseCase;
	private final CatalogServiceClient catalogServiceClient;
	private final StringRedisTemplate redisTemplate;
	private static final String ODDS_HASH_SESSION_PREFIX = "odds:hash:match:";
	
	@Transactional(readOnly = true)
	public Page<TicketBasicResponseDto> findAllByUser(Pageable pageable) {
		@SuppressWarnings("static-access")
		Page<TicketDomain> tickets = (Page<TicketDomain>) ticketDomainRepository
				.findByUserIdOrderByCreatedAtDesc(securityUtils.getCurrentUserId(), pageable);
		
		return tickets.map(mapper::toTicketBasicResponse);
	}

	@Transactional(readOnly = true)
	public TicketDetailsResponseDto findTicketDetailsById(Long ticketId) {
		TicketDomain ticket = ticketDomainRepository.findByIdWithItems(ticketId);
		
		Set<Long> matchIds = ticket.items().stream()
				.map(item -> item.matchId())
				.collect(Collectors.toSet());
		
		Map<Long, MatchDetailsDto> matchDetailsMap = catalogServiceClient
		        .findDetailsForTicket(matchIds)
		        .stream()
		        .collect(Collectors.toMap(
		            MatchDetailsDto::matchId,      
		            Function.identity(),   
		            (existing, replacement) -> existing
		        ));
		
		List<TicketItemDto> ticketItemDtos = new ArrayList<>();
		
		ticket.items().stream().forEach(item -> {
			MatchDetailsDto matchDetailsDto = matchDetailsMap.get(item.matchId());
			
			ticketItemDtos.add(
					new TicketItemDto(
							item.matchId(),
							matchDetailsDto.sportName(),
							matchDetailsDto.leagueName(),
							matchDetailsDto.homeTeam(),
							matchDetailsDto.awayTeam(),
							matchDetailsDto.startTime(),
							item.marketType(),
							item.oddValue(),
							item.status()
						));
		});
		
		return new TicketDetailsResponseDto(
				ticketId,
				ticket.type(),
				ticket.createdAt(),
				ticket.totalStake(),
				ticket.totalOdds(),
				ticket.potentialPayout(),
				ticketItemDtos
				);
	}

	@Transactional
	public TicketBasicResponseDto createTicket(@Valid CreateTicketRequestDto request) {
		@SuppressWarnings("static-access")
		Long userId = securityUtils.getCurrentUserId();
		
		List<OddResponseDto> changedOdds = 
				catalogServiceClient.checkTicketOdds(request.itemsAndOdds());
		
		if (!changedOdds.isEmpty()) {
			throw new OddsChangedException(changedOdds);
		}

		TicketDomain savedTicket = createTicketUseCase.execute(request, userId);
		return mapper.toTicketBasicResponse(savedTicket);
	}
	
	@Transactional
	public void processMatchFinished(MatchStatusChangedEvent event) {
		Set<Long> affectedTicketIds = new HashSet<>();
		List<Long> wonIds = new ArrayList<>();
		List<Long> lostIds = new ArrayList<>();
		Long matchId = event.matchId();
		
		List<TicketItemDomain> pendingItems = ticketItemDomainRepository.findPendingByMatchId(matchId);
		
	    pendingItems.forEach(item -> {
	        affectedTicketIds.add(item.ticketId());
	        
	        MarketEvaluatorStrategy strategy = marketEvaluatorService.getStrategy(item.marketType());
	        boolean isWon = strategy.evaluate(event.homeGoals(), event.awayGoals());
	        
	        TicketItemDomain updatedTicketItemDomain = isWon ? item.marAsWon() : item.markAsLost();
	        
	        if (updatedTicketItemDomain.status() == TicketItemStatus.WON) {
	            wonIds.add(updatedTicketItemDomain.id());
	        } else {
	            lostIds.add(updatedTicketItemDomain.id());
	        }
	    });
		
		if (!wonIds.isEmpty()) {
			ticketItemDomainRepository.updateStatus(wonIds, TicketItemStatus.WON);
		}
		
		if (!lostIds.isEmpty()) {
			ticketItemDomainRepository.updateStatus(lostIds, TicketItemStatus.LOST);
		}
		
		// redis delete odds for match
		redisTemplate.delete(ODDS_HASH_SESSION_PREFIX + matchId);
		
		ticketSettlementService.updateAndSettleTickets(affectedTicketIds, TicketUpdateMode.FINISHED);
	}
	
	@Transactional
	public void processMatchCancelled(MatchStatusChangedEvent event) {
		Set<Long> affectedTicketIds = new HashSet<>();
		Set<Long> cancelledIds = new HashSet<>();
		Long matchId = event.matchId();
		
		List<TicketItemDomain> pendingItems = ticketItemDomainRepository.findPendingByMatchId(matchId);
		
		pendingItems.forEach(item -> {
			affectedTicketIds.add(item.ticketId());
			cancelledIds.add(item.id());
		});

		ticketItemDomainRepository.updateStatusToCancelledByIds(cancelledIds);
		
		// redis delete odds for match
		redisTemplate.delete(ODDS_HASH_SESSION_PREFIX + matchId);
		
		ticketSettlementService.updateAndSettleTickets(affectedTicketIds, TicketUpdateMode.CANCELLED);
	}
	
}
