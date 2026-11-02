package com.wagerpro.catalogservice.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.wagerpro.catalogservice.application.dto.request.CreateMatchRequestDto;
import com.wagerpro.catalogservice.application.dto.request.UpdateMatchStatusRequestDto;
import com.wagerpro.catalogservice.application.dto.response.MatchDetailsResponseDto;
import com.wagerpro.catalogservice.application.dto.response.MatchOddsSummaryResponseDto;
import com.wagerpro.catalogservice.application.dto.response.MatchResponseDto;
import com.wagerpro.catalogservice.application.dto.response.UpcomingMatchesResponseDto;
import com.wagerpro.catalogservice.application.mapper.MatchApiMapper;
import com.wagerpro.catalogservice.application.usecase.UpdateMatchStatusUseCase;
import com.wagerpro.catalogservice.domain.model.MatchDomain;
import com.wagerpro.catalogservice.domain.repository.MatchDomainRepository;
import com.wagerpro.catalogservice.domain.validation.MatchValidation;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.MatchDetailsProjection;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.MatchOddsSummaryProjection;
import com.wagerpro.catalogservice.infrastructure.persistence.projection.UpcomingMatchesProjection;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {

	private final MatchDomainRepository matchDomainRepository;
	private final MatchApiMapper mapper;
	private final MatchValidation matchValidation;
	private final UpdateMatchStatusUseCase updateMatchStatusUseCase;

	@Transactional
	@CacheEvict(value = "upcomingMatches", key = "#sportName")
	public MatchResponseDto save(@Valid @RequestBody CreateMatchRequestDto matchRequestDto) {
		matchValidation.validateStartTime(matchRequestDto.startTime());
		MatchDomain matchDomain = matchDomainRepository.save(mapper.toDomain(matchRequestDto));
		return mapper.toResponseWithLeagueId(matchDomain);
	}
	
	@Transactional
	public List<MatchOddsSummaryResponseDto> findOddsByMatchId(Long matchId) {
		List<MatchOddsSummaryProjection> allOddsByMatchId = matchDomainRepository.findOddsByMatchId(matchId);
		return allOddsByMatchId.stream().map(x -> mapper.toResponseDto(x)).toList();
	}

	@Transactional
	@Cacheable(value = "upcomingMatches", key = "#sportName")
	public List<UpcomingMatchesResponseDto> findUpcomingMatchesBySport(String sportName) {
		List<Long> matchIds = matchDomainRepository.findUpcomingMatchIdsBySport(sportName.toUpperCase().trim());
		if (matchIds.isEmpty()) {
			return Collections.emptyList();
		}

		List<UpcomingMatchesProjection> projections = matchDomainRepository.findBasicOddsForMatchIds(matchIds);
		Map<Long, List<UpcomingMatchesProjection>> groupedByMatch = 
				projections.stream().collect(Collectors.groupingBy(UpcomingMatchesProjection::id));
		
		return mapToUpcomingMatchesResponseDto(groupedByMatch);
	}

	@Transactional
	public List<UpcomingMatchesResponseDto> findUpcomingMatchesBySportAndHours(
			String sportName, int hours, int page, int size) {
		
		// in most cases, we retrieve the data from the cache
		List<UpcomingMatchesResponseDto> upcomingMatchesByHours = 
				this.findUpcomingMatchesBySport(sportName).stream()
	            .filter(x -> x.startTime().compareTo(LocalDateTime.now().plusHours(hours)) <= 0)
	            .toList();
	    
	    int upcomingMatchesByHoursSize = upcomingMatchesByHours.size();
		int start = Math.min(page * size, upcomingMatchesByHoursSize);
	    int end = Math.min(start + size, upcomingMatchesByHoursSize);
	    
	    if (start >= end) {
	        return Collections.emptyList();
	    }
	    
	    return upcomingMatchesByHours.subList(start, end);
	}
	
	public MatchResponseDto updateStatus(UpdateMatchStatusRequestDto request) {
		MatchDomain savedMatch = updateMatchStatusUseCase.execute(request);
		return mapper.toResponseWithLeagueId(savedMatch);
    }
	
	@Transactional
	public List<MatchDetailsResponseDto> findDetailsForTicket(List<Long> matchIds) {
		List<MatchDetailsProjection> detailsForTicket = matchDomainRepository.findDetailsForTicket(matchIds);
		return mapper.toMatchDetailsResponseDtos(detailsForTicket);
	}
	
	private List<UpcomingMatchesResponseDto> mapToUpcomingMatchesResponseDto(
	        Map<Long, List<UpcomingMatchesProjection>> groupedByMatch) {

		return groupedByMatch.values().stream()
	            .map(rows -> {
	                UpcomingMatchesProjection firstRow = rows.getFirst();
	                Map<String, BigDecimal> basicOddsMap = rows.stream()
	                        .collect(Collectors.toMap(
	                                UpcomingMatchesProjection::type,
	                                UpcomingMatchesProjection::oddValue));

	                return new UpcomingMatchesResponseDto(
	                        firstRow.id(),
	                        firstRow.country(),
	                        firstRow.leagueName(),
	                        firstRow.homeTeam(),
	                        firstRow.awayTeam(),
	                        firstRow.startTime(),
	                        basicOddsMap
	                );
	            })
	            .toList();
	}
	
}
