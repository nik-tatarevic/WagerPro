package com.wagerpro.catalogservice.application.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.wagerpro.catalogservice.infrastructure.persistence.projection.UpcomingMatchesProjection;

import lombok.RequiredArgsConstructor;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MatchService {

	private final MatchDomainRepository matchDomainRepository;
	private final MatchApiMapper mapper;
	private final MatchValidation matchValidation;
	private final UpdateMatchStatusUseCase updateMatchStatusUseCase;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;
	private static final String UPCOMING_MATCHES_SESSION_PREFIX = "matches:upcoming:";
	private static final String ODDS_HASH_SESSION_PREFIX = "odds:hash:match:";
	private static final Duration UPCOMING_MATCHES_SESSION_TTL = Duration.ofMinutes(30);
	private static final Duration ODDS_SESSION_TTL = Duration.ofHours(12);

	@Transactional
	public MatchResponseDto save(CreateMatchRequestDto matchRequestDto) {
		matchValidation.validateStartTime(matchRequestDto.startTime());
		MatchDomain matchDomain = matchDomainRepository.save(mapper.toDomain(matchRequestDto));
		
		redisTemplate.delete(UPCOMING_MATCHES_SESSION_PREFIX + matchRequestDto.sportName().toUpperCase().trim());
		
		return mapper.toResponseWithLeagueId(matchDomain);
	}
	
	@Transactional(readOnly = true)
	public List<MatchOddsSummaryResponseDto> findOddsByMatchId(Long matchId) {
		String redisKey = ODDS_HASH_SESSION_PREFIX + matchId;
		Map<Object, Object> entries = redisTemplate.opsForHash().entries(redisKey);

	    if (entries != null && !entries.isEmpty()) {
	        return entries.values().stream()
	                .map(obj -> objectMapper.convertValue(obj, MatchOddsSummaryResponseDto.class))
	                .toList();
	    }

		List<MatchOddsSummaryResponseDto> allOddsByMatchId = 
				matchDomainRepository.findOddsByMatchId(matchId)
				.stream()
				.map(x -> mapper.toResponseDto(x))
				.toList();
		
		if (allOddsByMatchId.isEmpty()) {
	        return Collections.emptyList();
	    }
		
		Map<String, Object> oddsMap = allOddsByMatchId.stream()
	            .collect(Collectors.toMap(
	                dto -> String.valueOf(dto.id()),
	                dto -> dto 
	            ));
		
		redisTemplate.opsForHash().putAll(redisKey, oddsMap);
	    redisTemplate.expire(redisKey, ODDS_SESSION_TTL);
	    
		return allOddsByMatchId;
	}

	@Transactional(readOnly = true)
	public List<UpcomingMatchesResponseDto> findUpcomingMatchesBySport(String sportName) {
	    String key = UPCOMING_MATCHES_SESSION_PREFIX + sportName.toUpperCase().trim();

	    Object cachedObj = redisTemplate.opsForValue().get(key);
	    if (cachedObj != null) {
	        return objectMapper.convertValue(cachedObj, new TypeReference<List<UpcomingMatchesResponseDto>>() {});
	    }
		
		List<Long> matchIds = matchDomainRepository.findUpcomingMatchIdsBySport(sportName.toUpperCase().trim());
		if (matchIds.isEmpty()) {
			return Collections.emptyList();
		}

		List<UpcomingMatchesProjection> projections = matchDomainRepository.findBasicOddsForMatchIds(matchIds);
		Map<Long, List<UpcomingMatchesProjection>> groupedByMatch = 
				projections.stream().collect(Collectors.groupingBy(UpcomingMatchesProjection::id));
		
		List<UpcomingMatchesResponseDto> responseDtos = mapToUpcomingMatchesResponseDto(groupedByMatch);
		
	    redisTemplate.opsForValue().set(key, responseDtos, UPCOMING_MATCHES_SESSION_TTL);
	    
		return responseDtos;
	}

	@Transactional(readOnly = true)
	public List<UpcomingMatchesResponseDto> findUpcomingMatchesBySportAndHours(
			String sportName, int hours, int page, int size) {
		
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
	
	@Transactional
	public MatchResponseDto updateStatus(UpdateMatchStatusRequestDto request) {
		MatchDomain savedMatch = updateMatchStatusUseCase.execute(request);
		return mapper.toResponseWithLeagueId(savedMatch);
    }
	
	@Transactional(readOnly = true)
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
