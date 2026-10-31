package com.wagerpro.catalogservice.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagerpro.catalogservice.infrastructure.persistence.entity.OutboxEvent;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEvent, UUID> {

	List<OutboxEvent> findByProcessedFalse();

}