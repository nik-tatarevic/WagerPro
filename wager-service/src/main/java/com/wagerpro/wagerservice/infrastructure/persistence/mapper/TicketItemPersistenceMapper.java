package com.wagerpro.wagerservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.wagerpro.wagerservice.domain.model.TicketItemDomain;
import com.wagerpro.wagerservice.infrastructure.persistence.entity.TicketItem;

@Mapper(componentModel = "spring")
public interface TicketItemPersistenceMapper {
	TicketItem toEntity(TicketItemDomain ticketItemDomain);
	TicketItemDomain toDomain(TicketItem ticketItem);
}
