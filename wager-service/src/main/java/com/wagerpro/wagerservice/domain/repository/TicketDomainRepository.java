package com.wagerpro.wagerservice.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wagerpro.wagerservice.domain.model.TicketDomain;

public interface TicketDomainRepository {
	
	TicketDomain save(TicketDomain transaction);
    
    List<TicketDomain> findAllById(Iterable<Long> ids);
    
	void saveAll(List<TicketDomain> updatedTickets);
	
	Page<TicketDomain> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
	
	TicketDomain findByIdWithItems(Long ticketId);
}
