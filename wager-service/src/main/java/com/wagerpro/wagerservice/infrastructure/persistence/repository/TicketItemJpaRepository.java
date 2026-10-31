package com.wagerpro.wagerservice.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagerpro.wagerservice.infrastructure.persistence.entity.TicketItem;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketItemStatus;

public interface TicketItemJpaRepository extends JpaRepository<TicketItem, Long> {
	
    List<TicketItem> findByTicketId(Long ticketId);
    
    List<TicketItem> findByMatchIdAndStatus(Long matchId, TicketItemStatus status);
}
