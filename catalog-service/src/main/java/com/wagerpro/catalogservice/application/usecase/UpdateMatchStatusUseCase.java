package com.wagerpro.catalogservice.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.wagerpro.catalogservice.application.dto.request.UpdateMatchStatusRequestDto;
import com.wagerpro.catalogservice.domain.exception.MatchNotFoundException;
import com.wagerpro.catalogservice.domain.model.MatchDomain;
import com.wagerpro.catalogservice.domain.model.OutboxEventDomain;
import com.wagerpro.catalogservice.domain.repository.MatchDomainRepository;
import com.wagerpro.catalogservice.domain.repository.OutboxEventDomainRepository;
import com.wagerpro.catalogservice.infrastructure.kafka.event.MatchStatusChangedEvent;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class UpdateMatchStatusUseCase {
	
	private final MatchDomainRepository matchDomainRepository;
	private final OutboxEventDomainRepository outboxEventDomainRepository;
	private final ObjectMapper objectMapper;

	@Transactional
	public MatchDomain execute(UpdateMatchStatusRequestDto request) {
		MatchDomain match = matchDomainRepository.findById(request.matchId())
                .orElseThrow(() -> new MatchNotFoundException("Match not found with id: " + request.matchId()));
        
        MatchDomain savedMatch = matchDomainRepository.save(
        		match.changeStatus(request.status())
		);
        
        MatchStatusChangedEvent matchEvent = new MatchStatusChangedEvent(
        		savedMatch.id(), match.status(), request.status(), LocalDateTime.now().toString(), 3, 2);
        
        outboxEventDomainRepository.save(
        		new OutboxEventDomain(
	        		savedMatch.id().toString(), 
	        		"MatchStatusChangedEvent", 
	        		objectMapper.writeValueAsString(matchEvent),
	        		LocalDateTime.now(),
	        		false
		));
        
		return savedMatch;
	}

}
