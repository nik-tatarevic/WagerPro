package com.wagerpro.wagerservice.application.usecase;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagerpro.wagerservice.application.dto.request.CreateTicketRequestDto;
import com.wagerpro.wagerservice.application.mapper.TicketItemApiMapper;
import com.wagerpro.wagerservice.domain.model.TicketDomain;
import com.wagerpro.wagerservice.domain.model.TicketItemDomain;
import com.wagerpro.wagerservice.domain.repository.TicketDomainRepository;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TransactionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateTicketUseCase {
	
	private final TicketDomainRepository ticketDomainRepository;
	private final TicketItemApiMapper ticketItemApiMapper;
	private final WithdrawMoneyUseCase withdrawMoneyUseCase;

	@Transactional
	public TicketDomain execute(CreateTicketRequestDto request, Long userId) {
		List<TicketItemDomain> ticketItemsDomain = ticketItemApiMapper.toDomain(request.itemsAndOdds());
		BigDecimal totalStake = request.totalStake();
		
		TicketDomain ticketDomain = 
				new TicketDomain(null, userId, 
								request.type(), totalStake, 
								request.totalOdds(), request.potentialPayout(), 
								null, ticketItemsDomain, null, null);
		
		TicketDomain savedTicket = ticketDomainRepository.save(ticketDomain);
		
		withdrawMoneyUseCase.execute(userId, totalStake, TransactionType.BET_STAKE);
		
		return savedTicket;
	}
	
	
	
	

}
