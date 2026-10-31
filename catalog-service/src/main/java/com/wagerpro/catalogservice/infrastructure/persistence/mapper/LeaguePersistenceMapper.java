package com.wagerpro.catalogservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;

import com.wagerpro.catalogservice.domain.model.LeagueDomain;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.League;

@Mapper(componentModel = "spring")
public interface LeaguePersistenceMapper {

	League toEntity(LeagueDomain leagueDomain);
	
	LeagueDomain toDomain(League league);
	
}