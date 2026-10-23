package com.wagerpro.catalogservice.application.dto.response;

import java.time.LocalDateTime;

import com.wagerpro.catalogservice.infrastructure.persistence.enums.MatchStatus;

public record MatchResponseDto (
	
	Long leagueId,
	String homeTeam,
	String awayTeam,
	LocalDateTime startTime,
	MatchStatus status

) {}
