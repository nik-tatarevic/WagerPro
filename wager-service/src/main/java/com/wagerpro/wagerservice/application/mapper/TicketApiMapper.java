package com.wagerpro.wagerservice.application.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.mapstruct.Mapper;

import com.wagerpro.wagerservice.application.dto.response.MatchDetailsDto;
import com.wagerpro.wagerservice.application.dto.response.TicketBasicResponseDto;
import com.wagerpro.wagerservice.application.dto.response.TicketItemDto;
import com.wagerpro.wagerservice.domain.model.TicketDomain;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketType;

@Mapper(componentModel = "spring")
public interface TicketApiMapper {
	
	TicketBasicResponseDto toTicketBasicResponse(TicketDomain ticketDomain);
	
	List<TicketItemDto> toMatchDetailsDtos(List<MatchDetailsDto> matchDetailsDtos);
	
	TicketDomain toDomain(Long userId, BigDecimal totalStake, 
						  BigDecimal totalOdds, BigDecimal potentialPayout, 
						  TicketType type);

}
