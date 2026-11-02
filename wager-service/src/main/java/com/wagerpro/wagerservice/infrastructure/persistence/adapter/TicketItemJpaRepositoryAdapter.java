package com.wagerpro.wagerservice.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wagerpro.wagerservice.domain.model.TicketItemDomain;
import com.wagerpro.wagerservice.domain.repository.TicketItemDomainRepository;
import com.wagerpro.wagerservice.infrastructure.persistence.entity.Ticket;
import com.wagerpro.wagerservice.infrastructure.persistence.entity.TicketItem;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketItemStatus;
import com.wagerpro.wagerservice.infrastructure.persistence.mapper.TicketItemPersistenceMapper;
import com.wagerpro.wagerservice.infrastructure.persistence.repository.TicketItemJpaRepository;
import com.wagerpro.wagerservice.infrastructure.persistence.repository.TicketJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TicketItemJpaRepositoryAdapter implements TicketItemDomainRepository {
	
	private final TicketItemJpaRepository ticketItemJpaRepository;
	private final TicketItemPersistenceMapper mapper;
	private final TicketJpaRepository ticketJpaRepository;

	@Override
	public TicketItemDomain save(TicketItemDomain ticketItemDomain) {
		Ticket ticket = ticketJpaRepository.getReferenceById(ticketItemDomain.matchId());
		
		TicketItem ticketItem = mapper.toEntity(ticketItemDomain);
		ticketItem.setTicket(ticket);
		
		if (ticketItem.getStatus() == null) {
			ticketItem.setStatus(TicketItemStatus.PENDING);
		}
		
		return mapper.toDomain(ticketItemJpaRepository.save(ticketItem));
	}

	@Override
	public Optional<TicketItemDomain> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<TicketItemDomain> findByStatus(TicketItemStatus status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TicketItemDomain> findByMatchId(Long matchId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TicketItemDomain> findPendingByMatchId(Long matchId) {
		List<TicketItem> ticketItems = ticketItemJpaRepository.findByMatchIdAndStatus(matchId, TicketItemStatus.PENDING);
		return ticketItems.stream().map(item -> mapper.toDomain(item)).toList();
	}

	@Override
	public void saveAll(List<TicketItemDomain> updatedItems) {
		List<TicketItem> items = updatedItems.stream().map(item -> {
			Ticket ticket = ticketJpaRepository.getReferenceById(item.ticketId());

			TicketItem ticketItem = mapper.toEntity(item);
			ticketItem.setTicket(ticket);
			
			return ticketItem;
			
		}).toList();
		
		ticketItemJpaRepository.saveAll(items);
	}
	
	@Override
	@Transactional
	public void updateStatus(List<Long> ids, TicketItemStatus status) {
		ticketItemJpaRepository.updateStatusesByIds(ids, status);
	}
	
	@Override
	@Transactional
	public void updateStatusToCancelledByIds(Set<Long> ids) {
		ticketItemJpaRepository.updateStatusToCancelledByIds(ids);
	}
	
}
