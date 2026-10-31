package com.wagerpro.catalogservice.application.dto.response;

import java.time.LocalDateTime;

import com.wagerpro.catalogservice.infrastructure.persistence.enums.MatchStatus;

public record MatchDetailsResponseDto(
		Long matchId, 
		String sportName, 
		String leagueName, 
		String homeTeam,
		String awayTeam, 
		LocalDateTime startTime, 
		MatchStatus status
) {}
