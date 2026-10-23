package com.wagerpro.catalogservice.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wagerpro.catalogservice.infrastructure.persistence.entity.League;

public interface LeagueJpaRepository extends JpaRepository<League, Long> {

}
