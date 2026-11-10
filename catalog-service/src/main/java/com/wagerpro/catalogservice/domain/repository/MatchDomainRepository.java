package com.wagerpro.catalogservice.domain.repository;

import java.util.List;
import java.util.Optional;

import com.wagerpro.catalogservice.domain.model.MatchDomain;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.MatchDetailsProjection;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.MatchOddsSummaryProjection;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.UpcomingMatchesProjection;

public interface MatchDomainRepository {

	MatchDomain save(MatchDomain matchDomain);
	
	Optional<MatchDomain> findById(Long matchId);
	
	List<MatchDomain> findAll();
	
	List<MatchOddsSummaryProjection> findOddsByMatchId(Long matchId);
	
	List<MatchDomain> findUpcomingMatchesBySportAndHours(String sport, int hours);

	List<Long> findUpcomingMatchIdsBySport(String sportName);
	
	List<UpcomingMatchesProjection> findBasicOddsForMatchIds(List<Long> matchIds);
	
	List<MatchDetailsProjection> findDetailsForTicket(List<Long> matchIds);
}
