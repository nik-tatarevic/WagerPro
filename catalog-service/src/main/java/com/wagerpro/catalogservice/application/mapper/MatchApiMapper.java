package com.wagerpro.catalogservice.application.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.wagerpro.catalogservice.application.dto.request.CreateMatchRequestDto;
import com.wagerpro.catalogservice.application.dto.response.MatchDetailsResponseDto;
import com.wagerpro.catalogservice.application.dto.response.MatchOddsSummaryResponseDto;
import com.wagerpro.catalogservice.application.dto.response.MatchResponseDto;
import com.wagerpro.catalogservice.domain.model.MatchDomain;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.MatchDetailsProjection;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.MatchOddsSummaryProjection;

@Mapper(componentModel = "spring")
public interface MatchApiMapper {

	MatchDomain toDomain(CreateMatchRequestDto matchRequestDto);
	
	MatchOddsSummaryResponseDto toResponseDto(MatchOddsSummaryProjection matchOddsSummaryProjection);
	
	List<MatchDetailsResponseDto> toMatchDetailsResponseDtos(List<MatchDetailsProjection> projection);
	
	default MatchResponseDto toResponseWithLeagueId(MatchDomain matchDomain) {
	    return new MatchResponseDto(
	    		matchDomain.leagueId(),
				matchDomain.homeTeam(),
				matchDomain.awayTeam(),
				matchDomain.startTime(),
				matchDomain.status()
	    );
	}
	
}
