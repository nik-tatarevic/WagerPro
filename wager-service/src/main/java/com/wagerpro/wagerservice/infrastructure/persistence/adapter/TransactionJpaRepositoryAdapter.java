package com.wagerpro.wagerservice.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.wagerpro.wagerservice.domain.model.TransactionDomain;
import com.wagerpro.wagerservice.domain.repository.TransactionDomainRepository;
import com.wagerpro.wagerservice.infrastructure.persistence.entity.Transaction;
import com.wagerpro.wagerservice.infrastructure.persistence.entity.Wallet;
import com.wagerpro.wagerservice.infrastructure.persistence.mapper.TransactionPersistenceMapper;
import com.wagerpro.wagerservice.infrastructure.persistence.repository.TransactionJpaRepository;
import com.wagerpro.wagerservice.infrastructure.persistence.repository.WalletJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransactionJpaRepositoryAdapter implements TransactionDomainRepository {

    private final TransactionJpaRepository transactionJpaRepository;
    private final WalletJpaRepository walletJpaRepository;
    private final TransactionPersistenceMapper mapper;
    
	@Override
	public TransactionDomain save(TransactionDomain transactionDomain) {
		Wallet walletProxy = walletJpaRepository.getReferenceById(transactionDomain.walletId());
		
		Transaction transaction = mapper.toEntity(transactionDomain);
		transaction.setWallet(walletProxy);
		
		return mapper.toDomain(
				transactionJpaRepository.save(transaction)
		);
	}
	
	@Override
	public Optional<TransactionDomain> findById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
	
	@Override
	public List<TransactionDomain> findAllByUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<TransactionDomain> findAllByWalletId(Long walletId) {
		// TODO Auto-generated method stub
		return null;
	}

}
