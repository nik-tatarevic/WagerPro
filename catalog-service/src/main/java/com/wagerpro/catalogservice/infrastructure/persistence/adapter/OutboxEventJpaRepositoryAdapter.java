package com.wagerpro.catalogservice.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;

import com.wagerpro.catalogservice.domain.model.OutboxEventDomain;
import com.wagerpro.catalogservice.domain.repository.OutboxEventDomainRepository;
import com.wagerpro.catalogservice.infrastructure.persistence.entity.OutboxEvent;
import com.wagerpro.catalogservice.infrastructure.persistence.mapper.OutboxEventPersistenceMapper;
import com.wagerpro.catalogservice.infrastructure.persistence.repository.OutboxEventJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxEventJpaRepositoryAdapter implements OutboxEventDomainRepository {
	
	private final OutboxEventJpaRepository outboxEventJpaRepository;
	private final OutboxEventPersistenceMapper mapper;

	@Override
	public OutboxEventDomain save(OutboxEventDomain domain) {
		OutboxEvent entity = mapper.toEntity(domain);
		OutboxEvent savedEvent = outboxEventJpaRepository.save(entity);
		
		return mapper.toDomain(savedEvent);
	}

}
