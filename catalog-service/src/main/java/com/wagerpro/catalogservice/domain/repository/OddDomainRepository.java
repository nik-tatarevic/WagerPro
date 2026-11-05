package com.wagerpro.catalogservice.domain.repository;

import java.util.Optional;

import com.wagerpro.catalogservice.domain.model.OddDomain;

public interface OddDomainRepository {

	OddDomain saveOdd(OddDomain oddDomain);
	
	Optional<OddDomain> findByMatchIdAndMarketDefinitionId(Long matchId, Long marketDefinitionId);
	
	OddDomain updateOdd(OddDomain changedOdd);
	
	OddDomain findById(Long id);
	
}
