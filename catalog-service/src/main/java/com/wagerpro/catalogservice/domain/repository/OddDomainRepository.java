package com.wagerpro.catalogservice.domain.repository;

import java.math.BigDecimal;
import java.util.Optional;

import com.wagerpro.catalogservice.domain.model.OddDomain;

public interface OddDomainRepository {

	OddDomain saveOdd(OddDomain oddDomain);
	
	Optional<OddDomain> findByMatchIdAndMarketDefinitionId(Long matchId, Long marketDefinitionId);
	
	OddDomain updateOdd(OddDomain changedOdd);
	
	BigDecimal findOddValueById(Long id);
	
	OddDomain findById(Long id);
	
}
