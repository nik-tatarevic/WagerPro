package com.wagerpro.wagerservice.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.wagerpro.wagerservice.infrastructure.persistence.enums.TransactionType;

public record TransactionDomain(
        Long id,
        Long walletId,
        Long userId,
        BigDecimal amount,
        TransactionType type,
        LocalDateTime createdAt
) {
}
