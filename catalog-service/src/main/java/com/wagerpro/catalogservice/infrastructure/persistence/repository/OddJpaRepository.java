package com.wagerpro.catalogservice.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagerpro.catalogservice.infrastructure.persistence.entity.Odd;

public interface OddJpaRepository extends JpaRepository<Odd, Long> {
	
	Optional<Odd> findByMatchIdAndMarketDefinitionId(Long matchId, Long marketDefinitionId);

}
