package com.wagerpro.catalogservice.infrastructure.persistence.projection;

import java.math.BigDecimal;

import com.wagerpro.catalogservice.infrastructure.persistence.enums.MarketDefinitionType;

public record MatchOddsSummaryProjection(Long id, String type, BigDecimal oddValue) {
}
