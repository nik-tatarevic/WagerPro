package com.wagerpro.wagerservice.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wagerpro.wagerservice.infrastructure.persistence.entity.Ticket;

@Repository
public interface TicketJpaRepository extends JpaRepository<Ticket, Long> {
	Page<Ticket> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
	
//	Page<Ticket> findByUserIdAOrderByCreatedAtDesc(Long userId);
	
	List<Ticket> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
	
	@Query("SELECT t FROM Ticket t LEFT JOIN FETCH t.items WHERE t.id = :ticketId")
    Optional<Ticket> findByIdWithItems(@Param("ticketId") Long ticketId);
	
}
