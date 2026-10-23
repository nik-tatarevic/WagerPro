package com.wagerpro.wagerservice.application.service;

import org.springframework.stereotype.Service;

import com.wagerpro.wagerservice.application.dto.request.CreateDepositRequestDto;
import com.wagerpro.wagerservice.application.dto.request.CreateWithdrawRequestDto;
import com.wagerpro.wagerservice.application.dto.response.WalletResponseDto;
import com.wagerpro.wagerservice.application.usecase.DepositMoneyUseCase;
import com.wagerpro.wagerservice.application.usecase.WithdrawMoneyUseCase;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TransactionType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WalletService {
	
	private final DepositMoneyUseCase depositMoneyUseCase;
	private final WithdrawMoneyUseCase withdrawMoneyUseCase;

	public WalletResponseDto deposit(@Valid CreateDepositRequestDto request) {
		return depositMoneyUseCase.execute(request.userId(), request.amount(), TransactionType.DEPOSIT);
	}

	public WalletResponseDto withdraw(@Valid CreateWithdrawRequestDto request) {
		return withdrawMoneyUseCase.execute(request.userId(), request.amount(), TransactionType.WITHDRAWAL);
	}

}
