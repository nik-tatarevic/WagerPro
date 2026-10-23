package com.wagerpro.catalogservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.wagerpro.catalogservice.domain.model.SportDomain;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.Sport;


@Mapper(componentModel = "spring")
public interface SportPersistenceMapper {
	
	Sport toEntity(SportDomain sportDomain);
	
	SportDomain toDomain(Sport sport);
	
}
