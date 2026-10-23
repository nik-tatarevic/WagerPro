package com.wagerpro.catalogservice.infrastructure.persistence.scheduler;

import org.springframework.stereotype.Component;

import com.wagerpro.catalogservice.application.service.MatchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchStatusScheduler {
	
	private final MatchService matchService;

//    @Scheduled(fixedDelay = 60000)
    public void updateScheduledMatchesFromUpcomingToLive() {

        log.info("Starting scheduled match status update...");

        matchService.updateScheduledMatchesFromUpcomingToLive();

    }

}
