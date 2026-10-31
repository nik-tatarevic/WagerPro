package com.wagerpro.catalogservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.wagerpro.catalogservice.domain.model.OutboxEventDomain;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.OutboxEvent;

@Mapper(componentModel = "spring")
public interface OutboxEventPersistenceMapper {

	OutboxEvent toEntity(OutboxEventDomain domain);
	
	OutboxEventDomain toDomain(OutboxEvent outboxEvent);
	
	
}
