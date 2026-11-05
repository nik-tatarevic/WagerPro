package com.wagerpro.catalogservice.infrastructure.persistence.projection;

import java.math.BigDecimal;

public record MatchOddsSummaryProjection(
		Long id, 
		Long marketDefinitionId, 
		BigDecimal oddValue
) {}
