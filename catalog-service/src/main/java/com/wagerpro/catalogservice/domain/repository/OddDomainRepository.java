package com.wagerpro.catalogservice.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.wagerpro.catalogservice.domain.model.OddDomain;

public interface OddDomainRepository {

	OddDomain saveOdd(OddDomain oddDomain);
	
	Optional<OddDomain> findByMatchIdAndMarketDefinitionId(Long matchId, Long marketDefinitionId);
	
	OddDomain updateOdd(OddDomain changedOdd);
	
	OddDomain findById(Long id);

	List<OddDomain> findAllById(Set<Long> oddIds);
	
}
