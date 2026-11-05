package com.wagerpro.wagerservice.domain.repository;

import java.util.List;
import java.util.Set;

import com.wagerpro.wagerservice.domain.model.TicketItemDomain;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketItemStatus;

public interface TicketItemDomainRepository {
	
	TicketItemDomain save(TicketItemDomain ticketItemDomain);
	
	List<TicketItemDomain> findPendingByMatchId(Long matchId);
	
	void saveAll(List<TicketItemDomain> updatedItems);
	
	void updateStatus(List<Long> ids, TicketItemStatus status);

	void updateStatusToCancelledByIds(Set<Long> ids);
	
	
}