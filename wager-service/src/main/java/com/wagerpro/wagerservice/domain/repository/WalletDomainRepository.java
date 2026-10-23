package com.wagerpro.wagerservice.domain.repository;

import java.util.Optional;

import com.wagerpro.wagerservice.domain.model.WalletDomain;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.WalletStatus;

public interface WalletDomainRepository {
	WalletDomain save(WalletDomain wallet);
    Optional<WalletDomain> findById(Long id);
    Optional<WalletDomain> findByUserId(Long userId);
    void deleteById(Long id);
	boolean existWalletById(Long walletId);
	WalletStatus getWalletStatusById(Long walletId);
}
