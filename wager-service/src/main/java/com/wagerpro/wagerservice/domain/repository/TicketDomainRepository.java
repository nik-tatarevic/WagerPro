package com.wagerpro.wagerservice.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wagerpro.wagerservice.domain.model.TicketDomain;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketStatus;

public interface TicketDomainRepository {
	TicketDomain save(TicketDomain transaction);
//    TicketDomain findById(Long id);
//    Optional<TicketDomain> findByTicketItemId(Long ticketItemId);
//    List<TicketDomain> findByStatus(TicketStatus status);
//    List<TicketDomain> findAll();
//    List<TicketDomain> findAllLiveTicketsByMatchId(Long matchId);
    
    List<TicketDomain> findAllById(Iterable<Long> ids);
//    List<TicketDomain> saveAll(Iterable<Long> tickets);
	void saveAll(List<TicketDomain> updatedTickets);
	
	Page<TicketDomain> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
//	List<TicketDomain> findByUserIdAndStatus(Long userId, TicketStatus status);
	
	TicketDomain findByIdWithItems(Long ticketId);
    
}
