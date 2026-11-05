package com.wagerpro.catalogservice.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.wagerpro.catalogservice.domain.exception.OddNotFoundException;
import com.wagerpro.catalogservice.domain.model.OddDomain;
import com.wagerpro.catalogservice.domain.repository.OddDomainRepository;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.MarketDefinition;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.Match;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.Odd;
import com.wagerpro.catalogservice.infrastructure.persistence.mapper.OddPersistenceMapper;
import com.wagerpro.catalogservice.infrastructure.persistence.repository.MarketDefinitionJpaRepository;
import com.wagerpro.catalogservice.infrastructure.persistence.repository.MatchJpaRepository;
import com.wagerpro.catalogservice.infrastructure.persistence.repository.OddJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OddJpaRepositoryAdapter implements OddDomainRepository {
	
	private final OddJpaRepository oddJpaRepository;
	private final OddPersistenceMapper mapper;
	private final MatchJpaRepository matchJpaRepository;
	private final MarketDefinitionJpaRepository marketDefinitionJpaRepository;
	
	@Override
	public OddDomain saveOdd(OddDomain oddDomain) {
	    Match matchProxy = matchJpaRepository.getReferenceById(oddDomain.matchId());
	    MarketDefinition marketDefinitionProxy = 
	            marketDefinitionJpaRepository.getReferenceById(oddDomain.marketDefinitionId());
	    
	    Odd odd = Odd.builder()
	    		.match(matchProxy)
	    		.marketDefinition(marketDefinitionProxy)
    			.oddValue(oddDomain.oddValue())
	    		.isActive(oddDomain.isActive())
	    		.build();
	    
	    return mapper.toDomain(oddJpaRepository.save(odd));
	}
	
	@Override
	public OddDomain updateOdd(OddDomain oddDomain) {
	    Odd odd = mapper.toEntity(oddDomain);
		return mapper.toDomain(oddJpaRepository.save(odd));
	}

	@Override
	public Optional<OddDomain> findByMatchIdAndMarketDefinitionId(Long matchId, Long marketDefinitionId) {
		return oddJpaRepository.findByMatchIdAndMarketDefinitionId(matchId, marketDefinitionId)
				.map(mapper::toDomain);
	}

	@Override
	public OddDomain findById(Long id) {
		Odd odd = oddJpaRepository.findById(id)
				.orElseThrow(() -> new OddNotFoundException("Current odd was not found."));
		
		return mapper.toDomain(odd);
	}

}
