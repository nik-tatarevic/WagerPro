package com.wagerpro.catalogservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.wagerpro.catalogservice.domain.model.LeagueDomain;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.League;

// TODO replace all ReportingPolicy.IGNORE -> WARN ; TO RADIS ZADNJE, PRE KOMITA!
@Mapper(componentModel = "spring")
public interface LeaguePersistenceMapper {

	League toEntity(LeagueDomain leagueDomain);
	
	LeagueDomain toDomain(League league);
	
}