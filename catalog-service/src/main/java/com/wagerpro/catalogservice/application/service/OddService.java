package com.wagerpro.catalogservice.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagerpro.catalogservice.application.dto.ChangedOddsResponseDto;
import com.wagerpro.catalogservice.application.dto.OddsValueRequestDto;
import com.wagerpro.catalogservice.application.dto.request.CreateOrUpdateOddRequestDto;
import com.wagerpro.catalogservice.application.dto.response.OddResponseDto;
import com.wagerpro.catalogservice.application.mapper.OddApiMapper;
import com.wagerpro.catalogservice.domain.exception.OddNotFoundException;
import com.wagerpro.catalogservice.domain.model.OddDomain;
import com.wagerpro.catalogservice.domain.repository.OddDomainRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OddService {
	
	private final OddApiMapper mapper;
	private final OddDomainRepository oddDomainRepository;
	
	@Transactional
	public OddResponseDto saveOdd(CreateOrUpdateOddRequestDto request) {
		OddDomain oddDomain = mapper.toDomain(request);
		OddDomain savedOdd = oddDomainRepository.saveOdd(oddDomain);
		return mapper.toResponseDto(savedOdd);		
	}

	@Transactional
	public OddResponseDto updateOdd(CreateOrUpdateOddRequestDto request) {
		OddDomain oddDomain = oddDomainRepository
				.findByMatchIdAndMarketDefinitionId(request.matchId(), request.marketDefinitionId())
				.orElseThrow(() -> new OddNotFoundException("Current odd was not found."));
				
		OddDomain changedOdd = oddDomain.changeOdd(request.oddValue());
		return mapper.toResponseDto(oddDomainRepository.updateOdd(changedOdd));
	}

	@Transactional
	public List<ChangedOddsResponseDto> checkTicketOdds(List<OddsValueRequestDto> request) {
		List<ChangedOddsResponseDto> different = new ArrayList<>();
		
		request.stream().forEach(requestOddValue -> {
			OddDomain oddDomain = oddDomainRepository.findById(requestOddValue.oddId());
			
			if (requestOddValue.oddValue().compareTo(oddDomain.oddValue()) != 0 || oddDomain.isActive() == false) {
				different.add(
						new ChangedOddsResponseDto(
								requestOddValue.oddId(), 
								oddDomain.oddValue(), 
								oddDomain.isActive()));
			}
		});
		
		return different;
	}
	
}
