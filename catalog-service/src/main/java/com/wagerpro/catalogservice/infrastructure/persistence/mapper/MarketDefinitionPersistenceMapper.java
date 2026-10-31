package com.wagerpro.catalogservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.wagerpro.catalogservice.domain.model.MarketDefinitionDomain;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.MarketDefinition;


@Mapper(componentModel = "spring")
public interface MarketDefinitionPersistenceMapper {
	
	MarketDefinition toEntity(MarketDefinitionDomain marketDefinitionDomain);
	
	MarketDefinitionDomain toDomain(MarketDefinition marketDefinition);
	
}
