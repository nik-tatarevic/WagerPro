package com.wagerpro.catalogservice.infrastructure.persistence.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpcomingMatchesProjection (
	Long id,
	String country,
	String leagueName,
	String homeTeam,
	String awayTeam,
	LocalDateTime startTime,
	String type,
	BigDecimal oddValue) {
}
