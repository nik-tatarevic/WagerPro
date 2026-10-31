package com.wagerpro.wagerservice.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wagerpro.wagerservice.infrastructure.persistence.entity.Wallet;

@Repository
public interface WalletJpaRepository extends JpaRepository<Wallet, Long> {
	
    Optional<Wallet> findByUserId(Long userId);
    
    boolean existsByUserId(Long userId);
}
