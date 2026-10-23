package com.wagerpro.wagerservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.wagerpro.wagerservice.domain.model.TicketDomain;
import com.wagerpro.wagerservice.infrastructure.persistence.entity.Ticket;
import com.wagerpro.wagerservice.infrastructure.persistence.entity.TicketItem;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketItemStatus;

@Mapper(componentModel = "spring")
public interface TicketPersistenceMapper {
	
	Ticket toEntity(TicketDomain ticketDomain);
	TicketDomain toDomain(Ticket ticket);
	
	default Ticket toEntity(TicketDomain domain, TicketItemPersistenceMapper itemMapper) {
        if (domain == null) {
            return null;
        }

        Ticket ticket = Ticket.builder()
                .id(domain.id())
                .userId(domain.userId())
                .type(domain.type())
                .totalStake(domain.totalStake())
                .totalOdds(domain.totalOdds())
                .potentialPayout(domain.potentialPayout())
                .status(domain.status())
                .createdAt(domain.createdAt())
                .version(domain.version())
                .build();

        if (domain.items() != null) {
            domain.items().forEach(itemDomain -> {
                TicketItem itemEntity = itemMapper.toEntity(itemDomain);
                itemEntity.setStatus(TicketItemStatus.PENDING);
                ticket.addItem(itemEntity);
            });
        }

        return ticket;
    }
	
}
