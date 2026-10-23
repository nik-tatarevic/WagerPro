package com.wagerpro.wagerservice.application.dto.response;

import java.math.BigDecimal;

public record CreateWithdrawResponseDto(Long userId, BigDecimal amount) {

}
