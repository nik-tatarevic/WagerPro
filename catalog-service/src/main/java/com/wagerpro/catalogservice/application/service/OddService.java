package com.wagerpro.catalogservice.application.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagerpro.catalogservice.application.dto.ChangedOddsResponseDto;
import com.wagerpro.catalogservice.application.dto.OddsValueRequestDto;
import com.wagerpro.catalogservice.application.dto.request.CreateOrUpdateOddRequestDto;
import com.wagerpro.catalogservice.application.dto.response.OddResponseDto;
import com.wagerpro.catalogservice.application.dto.response.OddSummaryResponseDto;
import com.wagerpro.catalogservice.application.mapper.OddApiMapper;
import com.wagerpro.catalogservice.domain.model.OddDomain;
import com.wagerpro.catalogservice.domain.repository.OddDomainRepository;
import com.wagerpro.catalogservice.infrastructure.redis.CatalogRedisAdapter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OddService {
	
	private final OddApiMapper mapper;
	private final OddDomainRepository oddDomainRepository;
	private final CatalogRedisAdapter redisAdapter;
	
	@Transactional
	public OddResponseDto saveOdd(CreateOrUpdateOddRequestDto request) {
		OddDomain oddDomain = mapper.toDomain(request);
		OddDomain savedOdd = oddDomainRepository.saveOdd(oddDomain)
				.setMarketDefinitionId(request.marketDefinitionId());
		
		OddSummaryResponseDto oddDto = 
				mapper.toOddsSummaryResponseDto(savedOdd, request.marketDefinitionId());
	    redisAdapter.saveOddForMatch(request.matchId(), oddDto);
		
		return mapper.toResponseDto(savedOdd);		
	}

	@Transactional
	public OddResponseDto updateOdd(CreateOrUpdateOddRequestDto request) {
		OddDomain changedOdd = oddDomainRepository
				.findById(request.oddId())
				.changeOdd(request.oddValue());
		OddDomain updatedOdd = oddDomainRepository.updateOdd(changedOdd);
		
		OddSummaryResponseDto oddDto = mapper.toOddsSummaryResponseDto(updatedOdd, request.marketDefinitionId());
	    redisAdapter.saveOddForMatch(request.matchId(), oddDto);
		
		return mapper.toResponseDto(updatedOdd);
	}

	@Transactional(readOnly = true)
	public List<ChangedOddsResponseDto> checkTicketOdds(List<OddsValueRequestDto> request) {
	    List<ChangedOddsResponseDto> different = new ArrayList<>();
	    Map<Long, BigDecimal> missingOddsMap = new HashMap<>();

	    for (OddsValueRequestDto oddRequest : request) {
	    	Long oddId = oddRequest.oddId();
	        OddSummaryResponseDto oddRedisDto = 
	        		redisAdapter.findOddForMatch(oddRequest.matchId(), oddId);
	        
	        if (oddRedisDto != null) {
	            if (oddRequest.oddValue().compareTo(oddRedisDto.oddValue()) != 0) {
	                different.add(new ChangedOddsResponseDto(oddId, oddRedisDto.oddValue(), true));
	            }
	        } else {
	        	// not found in redis
	        	missingOddsMap.put(oddId, oddRequest.oddValue());
	        }
	    }
	    
	    if (!missingOddsMap.isEmpty()) {
	        List<OddDomain> odds = oddDomainRepository.findAllById(missingOddsMap.keySet());
	        
	        Map<Long, OddDomain> dbOddsMap = odds.stream()
	                .collect(Collectors.toMap(OddDomain::id, Function.identity()));
	        
	        for (Map.Entry<Long, BigDecimal> entry : missingOddsMap.entrySet()) {
	            Long oddId = entry.getKey();
	            BigDecimal sentValue = entry.getValue();
	            
	            OddDomain oddDomain = dbOddsMap.get(oddId);

	            if (sentValue.compareTo(oddDomain.oddValue()) != 0 || !oddDomain.isActive()) {
	                different.add(new ChangedOddsResponseDto(
	                        oddDomain.id(), 
	                        oddDomain.oddValue(), 
	                        oddDomain.isActive()));
	            }
	        }

	    }
	    
	    return different;
	}
	
}
