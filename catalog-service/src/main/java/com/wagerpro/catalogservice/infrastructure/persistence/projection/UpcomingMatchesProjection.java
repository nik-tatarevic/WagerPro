package com.wagerpro.catalogservice.infrastructure.persistence.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// TODO PROBAJ DA NE BUDE interface nego klasa, i onda u @Query trazis SELECT new... UpcomingMatchesProjection()
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
