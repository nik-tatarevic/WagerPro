package com.wagerpro.catalogservice.infrastructure.persistence.projection;

import java.time.LocalDateTime;

public interface MatchDetailsProjection {
    Long getMatchId();
    String getSportName();
    String getLeagueName();
    String getHomeTeam();
    String getAwayTeam();
    LocalDateTime getStartTime();
    String getStatus();
}
