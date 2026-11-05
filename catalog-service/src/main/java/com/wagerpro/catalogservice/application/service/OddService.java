package com.wagerpro.catalogservice.application.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagerpro.catalogservice.application.dto.ChangedOddsResponseDto;
import com.wagerpro.catalogservice.application.dto.OddsValueRequestDto;
import com.wagerpro.catalogservice.application.dto.request.CreateOrUpdateOddRequestDto;
import com.wagerpro.catalogservice.application.dto.response.MatchOddsSummaryResponseDto;
import com.wagerpro.catalogservice.application.dto.response.OddResponseDto;
import com.wagerpro.catalogservice.application.mapper.OddApiMapper;
import com.wagerpro.catalogservice.domain.model.OddDomain;
import com.wagerpro.catalogservice.domain.repository.OddDomainRepository;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class OddService {
	
	private final OddApiMapper mapper;
	private final OddDomainRepository oddDomainRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;
	private static final Duration ODDS_SESSION_TTL = Duration.ofHours(12);
	private static final String ODDS_HASH_SESSION_PREFIX = "odds:hash:match:";
	
	@Transactional
	public OddResponseDto saveOdd(CreateOrUpdateOddRequestDto request) {
		OddDomain oddDomain = mapper.toDomain(request);
		OddDomain savedOdd = oddDomainRepository.saveOdd(oddDomain);
		
		MatchOddsSummaryResponseDto oddDto = mapper.toOddsSummaryResponseDto(savedOdd);
		
		String redisKey = ODDS_HASH_SESSION_PREFIX + request.matchId();
	    String fieldKey = String.valueOf(oddDto.id());
	    
	    redisTemplate.opsForHash().put(redisKey, fieldKey, oddDto);
	    redisTemplate.expire(redisKey, ODDS_SESSION_TTL);
		
		return mapper.toResponseDto(savedOdd);		
	}

	@Transactional
	public OddResponseDto updateOdd(CreateOrUpdateOddRequestDto request) {
		OddDomain changedOdd = oddDomainRepository.findById(request.oddId())
				.changeOdd(request.oddValue());
		OddDomain updatedOdd = oddDomainRepository.updateOdd(changedOdd);
		
		MatchOddsSummaryResponseDto oddDto = mapper.toOddsSummaryResponseDto(updatedOdd);
		
		String redisKey = ODDS_HASH_SESSION_PREFIX + request.matchId();
	    String fieldKey = String.valueOf(oddDto.id());
	    
	    redisTemplate.opsForHash().put(redisKey, fieldKey, oddDto);
	    redisTemplate.expire(redisKey, ODDS_SESSION_TTL);
		
		return mapper.toResponseDto(updatedOdd);
	}

	@Transactional(readOnly = true)
	public List<ChangedOddsResponseDto> checkTicketOdds(List<OddsValueRequestDto> request) {
	    List<ChangedOddsResponseDto> different = new ArrayList<>();

	    for (OddsValueRequestDto oddRequest : request) {
	        String redisKey = ODDS_HASH_SESSION_PREFIX + oddRequest.matchId();
	        Map<Object, Object> entries = redisTemplate.opsForHash().entries(redisKey);
	        
	        if (entries != null && !entries.isEmpty()) {
	            Object cachedObj = entries.get(String.valueOf(oddRequest.oddId()));
	            
	            if (cachedObj != null) {
	                MatchOddsSummaryResponseDto oddRedisDto = objectMapper.convertValue(
	                        cachedObj, MatchOddsSummaryResponseDto.class
	                );
	                
	                if (oddRequest.oddValue().compareTo(oddRedisDto.oddValue()) != 0) {
	                    different.add(new ChangedOddsResponseDto(
	                            oddRequest.oddId(), 
	                            oddRedisDto.oddValue(), 
	                            true
	                    ));
	                }
	                continue;
	            }
	        }
	        
	        OddDomain oddDomain = oddDomainRepository.findById(oddRequest.oddId());
	        
	        if (oddRequest.oddValue().compareTo(oddDomain.oddValue()) != 0 || oddDomain.isActive() == false) {
				different.add(
						new ChangedOddsResponseDto(
								oddDomain.id(), 
								oddDomain.oddValue(), 
								oddDomain.isActive()));
			}
	    }
	    
	    return different;
	}
	
}
