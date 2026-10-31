package com.wagerpro.catalogservice.application.mapper;

import org.mapstruct.Mapper;

import com.wagerpro.catalogservice.application.dto.CreateStatusChangedOutBoxDto;
import com.wagerpro.catalogservice.domain.model.OutboxEventDomain;

@Mapper(componentModel = "spring")
public interface OutboxEventApiMapper {
	
	OutboxEventDomain toDomain(CreateStatusChangedOutBoxDto dto);

}
