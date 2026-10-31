package com.wagerpro.wagerservice.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wagerpro.wagerservice.infrastructure.persistence.entity.Transaction;

@Repository
public interface TransactionJpaRepository extends JpaRepository<Transaction, Long> {
	
    List<Transaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    List<Transaction> findByUserIdAndTypeOrderByCreatedAtDesc(Long userId, String type);
}
