package com.wagerpro.wagerservice.domain.repository;

import java.util.List;
import java.util.Optional;

import com.wagerpro.wagerservice.domain.model.TransactionDomain;

public interface TransactionDomainRepository {
	TransactionDomain save(TransactionDomain transaction);
    Optional<TransactionDomain> findById(Long id);
    List<TransactionDomain> findAllByUserId(Long userId);
    List<TransactionDomain> findAllByWalletId(Long walletId);
}
