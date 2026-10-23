package com.wagerpro.wagerservice.application.dto.response;

import java.time.LocalDateTime;

public record MatchDetailsDto(
		Long matchId,
		 String sportName,
		 String leagueName,
		 String homeTeam,
		 String awayTeam,
		 LocalDateTime startTime
		
		) {

}
