package com.wagerpro.catalogservice.application.mapper;

import org.mapstruct.Mapper;

import com.wagerpro.catalogservice.application.dto.request.CreateOrUpdateOddRequestDto;
import com.wagerpro.catalogservice.application.dto.response.OddResponseDto;
import com.wagerpro.catalogservice.application.dto.response.OddSummaryResponseDto;
import com.wagerpro.catalogservice.domain.model.OddDomain;

@Mapper(componentModel = "spring")
public interface OddApiMapper {
	
	OddDomain toDomain(CreateOrUpdateOddRequestDto oddCreateRequestDto);

	OddResponseDto toResponseDto(OddDomain oddDomain);
	
	default OddSummaryResponseDto toOddsSummaryResponseDto(OddDomain oddDomain, Long marketDefinitionId) {
		return new OddSummaryResponseDto(oddDomain.id(), marketDefinitionId, oddDomain.oddValue());
	}
	
}
