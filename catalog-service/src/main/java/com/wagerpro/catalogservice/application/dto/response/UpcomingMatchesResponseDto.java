package com.wagerpro.catalogservice.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record UpcomingMatchesResponseDto(
		Long id, 
		String country, 
		String leagueName, 
		String homeTeam,
		String awayTeam,
		LocalDateTime startTime,
		Map<String, BigDecimal> basicOdds
) {}