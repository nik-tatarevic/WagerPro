package com.wagerpro.wagerservice.infrastructure.kafka.event;

import com.wagerpro.wagerservice.infrastructure.persistence.enums.MatchStatus;

public record MatchStatusChangedEvent(
	    Long matchId,
	    MatchStatus oldStatus,
	    MatchStatus status,
	    String occurredAt,
	    Integer homeGoals,
	    Integer awayGoals
) {}
