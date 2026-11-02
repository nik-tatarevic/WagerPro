package com.wagerpro.wagerservice.application.usecase;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.wagerpro.wagerservice.application.dto.response.WalletResponseDto;
import com.wagerpro.wagerservice.application.mapper.WalletApiMapper;
import com.wagerpro.wagerservice.domain.exception.AmountException;
import com.wagerpro.wagerservice.domain.exception.WalletNotFoundException;
import com.wagerpro.wagerservice.domain.model.TransactionDomain;
import com.wagerpro.wagerservice.domain.model.WalletDomain;
import com.wagerpro.wagerservice.domain.repository.TransactionDomainRepository;
import com.wagerpro.wagerservice.domain.repository.WalletDomainRepository;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TransactionType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepositMoneyUseCase {

	private final WalletDomainRepository walletDomainRepository;
	private final TransactionDomainRepository transactionDomainRepository;
	private final WalletApiMapper walletApiMapper;

	@Transactional
	public WalletResponseDto execute(Long userId, BigDecimal amount, TransactionType transactionType) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new AmountException("The amount must be greater than 0.");
		}
		
		WalletDomain walletDomain = walletDomainRepository.findByUserId(userId)
				.orElseThrow(() -> new WalletNotFoundException("" + userId))
				.deposit(amount);
		
		WalletDomain savedWallet = walletDomainRepository.save(walletDomain);
		
		transactionDomainRepository.save(
					new TransactionDomain(null, 
										  savedWallet.id(), 
										  userId, 
										  amount, 
										  transactionType, 
										  LocalDateTime.now()));

		return walletApiMapper.toResponseDto(savedWallet);
	}

}
