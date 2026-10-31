package com.wagerpro.wagerservice.application.dto.request;

import java.math.BigDecimal;

import com.wagerpro.wagerservice.infrastructure.persistence.enums.TransactionType;

public record CreateTransactionRequestDto(
        Long walletId,
        Long userId,
        BigDecimal amount,
        TransactionType type
) {}
