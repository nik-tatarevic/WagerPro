package com.wagerpro.catalogservice.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.wagerpro.catalogservice.domain.model.MatchDomain;
import com.wagerpro.catalogservice.domain.repository.MatchDomainRepository;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.League;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.Match;
import com.wagerpro.catalogservice.infrastructure.persistence.enums.MatchStatus;
import com.wagerpro.catalogservice.infrastructure.persistence.mapper.MatchPersistenceMapper;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.MatchDetailsProjection;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.MatchOddsSummaryProjection;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.UpcomingMatchesProjection;
import com.wagerpro.catalogservice.infrastructure.persistence.repository.LeagueJpaRepository;
import com.wagerpro.catalogservice.infrastructure.persistence.repository.MatchJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MatchJpaRepositoryAdapter implements MatchDomainRepository {
	
	private final MatchJpaRepository matchJpaRepository;
	private final MatchPersistenceMapper mapper;
	private final LeagueJpaRepository leagueJpaRepository;

	@Override
    public MatchDomain save(MatchDomain matchDomain) {
        League leagueProxy = leagueJpaRepository.getReferenceById(matchDomain.leagueId());
        Match match = mapper.toEntity(matchDomain);
        match.setLeague(leagueProxy);
        
        if (match.getStatus() == null) {
        	match.setStatus(MatchStatus.UPCOMING);;
        }
        
        return mapper.toDomain(matchJpaRepository.save(match));
    }
	
	@Override
    public Optional<MatchDomain> findById(Long id) {
        return matchJpaRepository.findById(id).map(mapper::toDomainWithLeagueId);
//        return matchJpaRepository.findById(id).map(mapper::toDomain);
    }

	@Override
	public List<MatchDomain> findAll() {
		return null;
	}

	@Override
	public List<MatchDomain> findUpcomingMatchesBySportAndHours(String sport, int hours) {
		return null;
	}
	
	public List<MatchOddsSummaryProjection> findOddsByMatchId(Long matchId) {
		return matchJpaRepository.findOddsByMatchId(matchId);
	}

	@Override
	public List<Long> findUpcomingMatchIdsBySport(String sportName) {
		return matchJpaRepository.findUpcomingMatchIdsBySport(sportName);
	}

	@Override
	public List<UpcomingMatchesProjection> findBasicOddsForMatchIds(List<Long> matchIds) {
		return matchJpaRepository.findBasicOddsForMatchIds(matchIds);
	}

	@Override
	public List<MatchDetailsProjection> findDetailsForTicket(List<Long> userIds) {
		return matchJpaRepository.findDetailsForTicket(userIds);
	}


}
