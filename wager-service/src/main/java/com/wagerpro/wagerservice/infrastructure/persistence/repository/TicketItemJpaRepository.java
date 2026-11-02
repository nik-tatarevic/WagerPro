package com.wagerpro.wagerservice.infrastructure.persistence.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wagerpro.wagerservice.infrastructure.persistence.entity.TicketItem;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketItemStatus;

public interface TicketItemJpaRepository extends JpaRepository<TicketItem, Long> {
	
    List<TicketItem> findByTicketId(Long ticketId);
    
    List<TicketItem> findByMatchIdAndStatus(Long matchId, TicketItemStatus status);
    
//    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Modifying
    @Query("UPDATE TicketItem t SET t.status = :status WHERE t.id IN :ids")
    void updateStatusesByIds(@Param("ids") List<Long> ids, @Param("status") TicketItemStatus status);
    
    @Modifying
    @Query("UPDATE TicketItem t SET t.status = 'CANCELLED', t.oddValue = 1.00 WHERE t.id IN :ids")
    void updateStatusToCancelledByIds(@Param("ids") Set<Long> ids);
    
}
