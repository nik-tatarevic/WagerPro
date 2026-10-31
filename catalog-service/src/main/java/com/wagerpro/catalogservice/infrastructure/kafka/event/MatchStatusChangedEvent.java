package com.wagerpro.catalogservice.infrastructure.kafka.event;

import com.wagerpro.catalogservice.infrastructure.persistence.enums.MatchStatus;

public record MatchStatusChangedEvent(
        Long matchId,
        MatchStatus oldStatus,
        MatchStatus status,
        String occurredAt,
        Integer homeGoals,
        Integer awayGoals
) {}
