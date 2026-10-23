package com.wagerpro.wagerservice.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.wagerpro.wagerservice.application.dto.ItemOddDetailsDto;
import com.wagerpro.wagerservice.domain.model.TicketItemDomain;

@Mapper(componentModel = "spring")
public interface TicketItemApiMapper {
	
	@Mapping(target = "ticketId", source = "ticketId")
	TicketItemDomain toDomain(ItemOddDetailsDto itemOddDetailsDto);
	
	List<TicketItemDomain> toDomain(List<ItemOddDetailsDto> itemOddDetailsDto);

}
