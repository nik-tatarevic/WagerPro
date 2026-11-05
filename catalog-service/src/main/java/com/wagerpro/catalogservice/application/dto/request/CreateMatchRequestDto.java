package com.wagerpro.catalogservice.application.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMatchRequestDto (
		@NotNull(message = "League ID must not be null")
        Long leagueId,

        @NotBlank(message = "Home team must not be blank")
        String homeTeam,

        @NotBlank(message = "Away team must not be blank")
        String awayTeam,

        @NotNull(message = "Start time must not be null")
        @Future(message = "Start time must be in the future")
        LocalDateTime startTime,
        
        String sportName
) {}