package com.wagerpro.catalogservice.domain.model;

import java.time.LocalDateTime;

import com.wagerpro.catalogservice.domain.exception.InvalidMatchStatusException;
import com.wagerpro.catalogservice.infrastructure.persistence.enums.MatchStatus;

public record MatchDomain(
		Long id, 
		Long leagueId, 
		String homeTeam, 
		String awayTeam, 
		LocalDateTime startTime,
		MatchStatus status, 
		Long version) {

	public MatchDomain changeStatus(MatchStatus newStatus) {
		ensureStatus(newStatus);
		return new MatchDomain(id, leagueId, homeTeam, awayTeam, startTime, newStatus, version);
	}

	private void ensureStatus(MatchStatus newStatus) {
		if (status == MatchStatus.FINISHED || status == MatchStatus.CANCELLED) {
			throw new InvalidMatchStatusException("Cannot change status of finished or cancelled match.");
		}
		if (status == MatchStatus.LIVE && newStatus == MatchStatus.UPCOMING) {
			throw new InvalidMatchStatusException("Cannot change status of live match to upcoming.");
		}
	}

	public boolean isLive() {
		return status == MatchStatus.LIVE;
	}

	public boolean isFinished() {
		return status == MatchStatus.FINISHED;
	}

	public boolean canAcceptTickets() {
		return status == MatchStatus.UPCOMING;
	}
	
}
