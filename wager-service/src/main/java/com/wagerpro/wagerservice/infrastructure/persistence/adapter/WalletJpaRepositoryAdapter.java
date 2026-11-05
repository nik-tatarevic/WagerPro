package com.wagerpro.wagerservice.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.wagerpro.wagerservice.domain.model.WalletDomain;
import com.wagerpro.wagerservice.domain.repository.WalletDomainRepository;
import com.wagerpro.wagerservice.infrastructure.persistence.entity.Wallet;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.WalletStatus;
import com.wagerpro.wagerservice.infrastructure.persistence.mapper.WalletPersistenceMapper;
import com.wagerpro.wagerservice.infrastructure.persistence.repository.WalletJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WalletJpaRepositoryAdapter implements WalletDomainRepository {

    private final WalletJpaRepository walletJpaRepository;
    private final WalletPersistenceMapper mapper;

    @Override
    public WalletDomain save(WalletDomain walletDomain) {
    	Wallet entity = mapper.toEntity(walletDomain);
    	Wallet savedEntity = walletJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<WalletDomain> findById(Long id) {
        return walletJpaRepository.findById(id)
        		.map(mapper::toDomain);
    }

    @Override
    public Optional<WalletDomain> findByUserId(Long userId) {
        return walletJpaRepository.findByUserId(userId)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        walletJpaRepository.deleteById(id);
    }

	@Override
	public WalletStatus getWalletStatusById(Long walletId) {
		// TODO Auto-generated method stub
		return null;
	}
}
