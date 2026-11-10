package com.wagerpro.catalogservice.infrastructure.redis;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.wagerpro.catalogservice.application.dto.response.OddSummaryResponseDto;
import com.wagerpro.catalogservice.application.dto.response.UpcomingMatchesResponseDto;

import lombok.RequiredArgsConstructor;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class CatalogRedisAdapter {
	
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;
	private static final String UPCOMING_MATCHES_SESSION_PREFIX = "matches:upcoming:";
	private static final String ODDS_HASH_SESSION_PREFIX = "odds:hash:match:";
	private static final Duration UPCOMING_MATCHES_SESSION_TTL = Duration.ofMinutes(30);
	private static final Duration ODDS_SESSION_TTL = Duration.ofHours(12);
	
	public void deleteKeyBySportName(String sportName) {
		redisTemplate.delete(UPCOMING_MATCHES_SESSION_PREFIX + sportName);
	}

	public void setOddsForMatch(Long matchId, List<OddSummaryResponseDto> allOddsByMatchId) {
		String redisKey = ODDS_HASH_SESSION_PREFIX + matchId;
		Map<String, Object> oddsMap = allOddsByMatchId.stream()
	            .collect(Collectors.toMap(
	                dto -> String.valueOf(dto.id()),
	                Function.identity()
	            ));
		
		redisTemplate.opsForHash().putAll(redisKey, oddsMap);
	    redisTemplate.expire(redisKey, ODDS_SESSION_TTL);
	}

	public List<OddSummaryResponseDto> findOddsForMatch(Long matchId) {
		String redisKey = ODDS_HASH_SESSION_PREFIX + matchId;
		Map<Object, Object> entries = redisTemplate.opsForHash().entries(redisKey);

	    if (entries != null && !entries.isEmpty()) {
	        return entries.values().stream()
	                .map(obj -> objectMapper.convertValue(obj, OddSummaryResponseDto.class))
	                .toList();
	    }
	    
	    return List.of();
	}

	public void setUpcomingMatchesBySport(String sportName, List<UpcomingMatchesResponseDto> responseDtos) {
		String redisKey = UPCOMING_MATCHES_SESSION_PREFIX + sportName.toUpperCase().trim();
		redisTemplate.opsForValue().set(redisKey, responseDtos, UPCOMING_MATCHES_SESSION_TTL);
	}

	public List<UpcomingMatchesResponseDto> findUpcomingMatchesBySport(String sportName) {
		String key = UPCOMING_MATCHES_SESSION_PREFIX + sportName.toUpperCase().trim();
	    Object cachedObj = redisTemplate.opsForValue().get(key);
	    
	    if (cachedObj != null) {
	        return objectMapper.convertValue(cachedObj, 
	        		new TypeReference<List<UpcomingMatchesResponseDto>>() {});
	    }
	    
	    return List.of();
	}

	public void saveOddForMatch(Long matchId, OddSummaryResponseDto oddDto) {
		String redisKey = ODDS_HASH_SESSION_PREFIX + matchId;
	    String fieldKey = String.valueOf(oddDto.id());
	    redisTemplate.opsForHash().put(redisKey, fieldKey, oddDto);
	    redisTemplate.expire(redisKey, ODDS_SESSION_TTL);
	}

	public Map<Object, Object> findOddsEntriesForMatch(Long matchId) {
		String redisKey = ODDS_HASH_SESSION_PREFIX + matchId;
        return redisTemplate.opsForHash().entries(redisKey);
	}

	public OddSummaryResponseDto findOddForMatch(Long matchId, Long oddId) {
		String redisKey = ODDS_HASH_SESSION_PREFIX + matchId;
		Object cachedObj = redisTemplate.opsForHash().get(redisKey, String.valueOf(oddId));
		
		if (cachedObj != null) {
			return objectMapper.convertValue(cachedObj, OddSummaryResponseDto.class);
		}
		
		return null;
	}
	
}
	
