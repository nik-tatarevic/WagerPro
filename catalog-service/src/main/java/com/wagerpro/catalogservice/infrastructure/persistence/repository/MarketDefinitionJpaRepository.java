package com.wagerpro.catalogservice.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagerpro.catalogservice.infrastructure.persistence.entity.MarketDefinition;

public interface MarketDefinitionJpaRepository extends JpaRepository<MarketDefinition, Long> {

}
