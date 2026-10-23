package com.wagerpro.wagerservice.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagerpro.wagerservice.application.dto.request.CreateDepositRequestDto;
import com.wagerpro.wagerservice.application.dto.request.CreateWithdrawRequestDto;
import com.wagerpro.wagerservice.application.dto.response.WalletResponseDto;
import com.wagerpro.wagerservice.application.service.WalletService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
public class WalletController {
	
	private final WalletService walletService;
	
	@PostMapping("/deposit")
    public ResponseEntity<WalletResponseDto> deposit(@Valid @RequestBody CreateDepositRequestDto request) {
		WalletResponseDto response = walletService.deposit(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
	
	@PostMapping("/withdraw")
	public ResponseEntity<WalletResponseDto> withdraw(@Valid @RequestBody CreateWithdrawRequestDto request) {
		WalletResponseDto response = walletService.withdraw(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}
