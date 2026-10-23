package com.wagerpro.wagerservice.infrastructure.kafka;

public record MatchFinishedEvent(
        Long matchId,
        Integer homeGoals,
        Integer awayGoals
) {}
