package com.wagerpro.wagerservice.application.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagerpro.wagerservice.application.usecase.DepositMoneyUseCase;
import com.wagerpro.wagerservice.domain.model.TicketDomain;
import com.wagerpro.wagerservice.domain.repository.TicketDomainRepository;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketStatus;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketUpdateMode;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TransactionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketSettlementService {

	private final DepositMoneyUseCase depositMoneyUseCase;
	private final TicketDomainRepository ticketDomainRepository;
	
	@Transactional
	public void updateAndSettleTickets(Set<Long> ticketIds, TicketUpdateMode ticketUpdateMode) {
	    if (ticketIds == null || ticketIds.isEmpty()) {
	        return;
	    }

	    List<TicketDomain> tickets = ticketDomainRepository.findAllById(ticketIds);
	    List<TicketDomain> processedTickets = tickets.stream().map(ticket -> {
	        TicketDomain current = 
	        		ticketUpdateMode == TicketUpdateMode.CANCELLED 
	        			? ticket.updatePotentialPayout() : ticket;
	        	
	        	return current.calculateStatus();
	        
	    	})
    		.filter(ticket -> ticket.status() != TicketStatus.PENDING)
    		.toList();
	    
	    ticketDomainRepository.saveAll(processedTickets);
	    
	    settleTicket(processedTickets.stream()
			    		.filter(ticket -> ticket.status() == TicketStatus.WON)
			    		.toList());
	}
	
	@Transactional
	private void settleTicket(List<TicketDomain> ticketList) {
		ticketList.stream().forEach(ticket -> {
			depositMoneyUseCase.execute(ticket.userId(), ticket.potentialPayout(), TransactionType.WIN_PAYOUT);
		});
	}
	
}
