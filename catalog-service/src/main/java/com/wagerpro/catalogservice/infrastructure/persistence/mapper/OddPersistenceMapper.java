package com.wagerpro.catalogservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.wagerpro.catalogservice.domain.model.OddDomain;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.Odd;

@Mapper(componentModel = "spring")
public interface OddPersistenceMapper {
	
	Odd toEntity(OddDomain oddDomain);
	
	@Mapping(target = "isActive", source = "active")
	OddDomain toDomain(Odd odd);

}
