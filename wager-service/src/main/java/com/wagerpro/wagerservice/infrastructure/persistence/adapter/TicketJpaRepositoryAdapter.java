package com.wagerpro.wagerservice.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.wagerpro.wagerservice.domain.exception.TicketNotFoundException;
import com.wagerpro.wagerservice.domain.model.TicketDomain;
import com.wagerpro.wagerservice.domain.repository.TicketDomainRepository;
import com.wagerpro.wagerservice.infrastructure.persistence.entity.Ticket;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketStatus;
import com.wagerpro.wagerservice.infrastructure.persistence.mapper.TicketItemPersistenceMapper;
import com.wagerpro.wagerservice.infrastructure.persistence.mapper.TicketPersistenceMapper;
import com.wagerpro.wagerservice.infrastructure.persistence.repository.TicketJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TicketJpaRepositoryAdapter implements TicketDomainRepository {
	
	private final TicketJpaRepository ticketJpaRepository;
	private final TicketPersistenceMapper mapper;
	private final TicketItemPersistenceMapper ticketItemPersistenceMapper;

	@Override
	public TicketDomain save(TicketDomain ticketDomain) {
		Ticket ticket = mapper.toEntity(ticketDomain, ticketItemPersistenceMapper);
		
		if (ticket.getStatus() == null) {
			ticket.setStatus(TicketStatus.PENDING);
        }
		
		return mapper.toDomain(ticketJpaRepository.save(ticket));
	}

	@Override
	public List<TicketDomain> findAllById(Iterable<Long> ids) {
		List<Ticket> tickets = ticketJpaRepository.findAllById(ids);
		return tickets.stream().map(ticket -> mapper.toDomain(ticket)).toList();
	}

	@Override
	public void saveAll(List<TicketDomain> updatedTickets) {
		List<Ticket> tickets = updatedTickets.stream().map(domain -> {
	        Ticket ticket = mapper.toEntity(domain);
	        
	        if (ticket.getItems() != null) {
	            ticket.getItems().forEach(item -> item.setTicket(ticket));
	        }
	        
	        return ticket;
	        
	    }).toList();

		ticketJpaRepository.saveAll(tickets);
	}

	@Override
	public Page<TicketDomain> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable) {
	    Page<Ticket> tickets =
	            ticketJpaRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

	    return tickets.map(mapper::toDomain);
	}

	@Override
//	@Transactional(readOnly = true)
	public TicketDomain findByIdWithItems(Long ticketId) {
		Ticket ticket = ticketJpaRepository.findByIdWithItems(ticketId).
				orElseThrow(() -> 
					new TicketNotFoundException("Ticket id: $s not found ".formatted(ticketId))
				);
		
		return mapper.toDomain(ticket);
	}

}
