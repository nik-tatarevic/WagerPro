package com.wagerpro.wagerservice.domain.repository;

import java.util.List;
import java.util.Optional;

import com.wagerpro.wagerservice.domain.model.TicketItemDomain;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketItemStatus;

public interface TicketItemDomainRepository {
	
	TicketItemDomain save(TicketItemDomain ticketItemDomain);
	
    Optional<TicketItemDomain> findById(Long id);
    
    List<TicketItemDomain> findByStatus(TicketItemStatus status);
    
    List<TicketItemDomain> findByMatchId(Long matchId);
    
	List<TicketItemDomain> findPendingByMatchId(Long matchId);
	
	void saveAll(List<TicketItemDomain> updatedItems);
}
