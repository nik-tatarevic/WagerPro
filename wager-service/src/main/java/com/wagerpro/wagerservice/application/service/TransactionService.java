package com.wagerpro.wagerservice.application.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.wagerpro.wagerservice.domain.exception.WalletNotFoundException;
import com.wagerpro.wagerservice.domain.model.TransactionDomain;
import com.wagerpro.wagerservice.domain.model.WalletDomain;
import com.wagerpro.wagerservice.domain.repository.TransactionDomainRepository;
import com.wagerpro.wagerservice.domain.repository.WalletDomainRepository;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TransactionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
	
	private final TransactionDomainRepository transactionDomainRepository;
	private final WalletDomainRepository walletDomainRepository;

	public void createTransaction(Long userId, BigDecimal amount, TransactionType transactionType) {
		WalletDomain walletDomain = walletDomainRepository.findByUserId(userId)
				.orElseThrow(() -> new WalletNotFoundException("" + userId));

		TransactionDomain transactionDomain = new TransactionDomain(
				null, 
				walletDomain.id(), 
				userId, 
				amount,
				transactionType, 
				null);
		
		transactionDomainRepository.save(transactionDomain);
	}
	
}
