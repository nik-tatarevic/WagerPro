package com.wagerpro.wagerservice.domain.model;

import java.math.BigDecimal;

import com.wagerpro.wagerservice.domain.exception.InsufficientBalanceException;
import com.wagerpro.wagerservice.domain.exception.WalletBlockedException;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.WalletStatus;

public record WalletDomain(
		Long id, 
		Long userId, 
		BigDecimal balance, 
		String currency, 
		WalletStatus status,
		Long version
) {

	public WalletDomain deposit(BigDecimal amount) {
		ensureBlockStatus();
		return new WalletDomain(id, userId, balance.add(amount), currency, status, version);
	}

	public WalletDomain withdraw(BigDecimal amount) {
		ensureBlockStatus();
		if (balance.compareTo(amount) < 0) {
			throw new InsufficientBalanceException("You do not have sufficient funds in your account.");
		}
		return new WalletDomain(id, userId, balance.subtract(amount), currency, status, version);
	}

	private void ensureBlockStatus() {
		if (status == WalletStatus.BLOCKED) {
			throw new WalletBlockedException("The wallet is blocked. The transaction was unsuccessful.");
		}
	}
	
}
