package com.wagerpro.wagerservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.wagerpro.wagerservice.domain.model.TicketItemDomain;
import com.wagerpro.wagerservice.infrastructure.persistence.entity.TicketItem;

@Mapper(componentModel = "spring")
public interface TicketItemPersistenceMapper {
	
	TicketItem toEntity(TicketItemDomain ticketItemDomain);
	
	default TicketItemDomain toDomain(TicketItem entity) {
	    return new TicketItemDomain(
	        entity.getId(),
	        entity.getTicket() != null ? entity.getTicket().getId() : null,
	        entity.getMatchId(),
	        entity.getOddId(),
	        entity.getOddValue(),
	        entity.getMarketType(),
	        entity.getStatus()
	    );
	}
	
}
