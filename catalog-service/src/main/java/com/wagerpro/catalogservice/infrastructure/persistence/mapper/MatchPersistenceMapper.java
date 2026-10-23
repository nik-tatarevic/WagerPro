package com.wagerpro.catalogservice.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.wagerpro.catalogservice.domain.model.MatchDomain;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.Match;

@Mapper(componentModel = "spring")
public interface MatchPersistenceMapper {
	
	Match toEntity(MatchDomain matchDomain);

	@Mapping(target = "leagueId", source = "league.id")
	MatchDomain toDomain(Match match);
	
	default MatchDomain toDomainWithLeagueId(Match entity) {
	    return new MatchDomain(
	        entity.getId(),
	        entity.getLeague() != null ? entity.getLeague().getId() : null,
	        entity.getHomeTeam(),
	        entity.getAwayTeam(),
	        entity.getStartTime(),
	        entity.getStatus(),
	        entity.getVersion()
	    );
	}

}
