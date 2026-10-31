package com.wagerpro.catalogservice.domain.repository;

import com.wagerpro.catalogservice.domain.model.OutboxEventDomain;

public interface OutboxEventDomainRepository {
	
	OutboxEventDomain save(OutboxEventDomain domain);

}
