package com.wagerpro.wagerservice.infrastructure.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.wagerpro.wagerservice.application.service.TicketService;
import com.wagerpro.wagerservice.infrastructure.kafka.event.MatchStatusChangedEvent;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class MatchFinishedConsumer {

	private final TicketService ticketService;
	private final ObjectMapper objectMapper;
	
    @KafkaListener(
		topics = "${app.kafka.topic.match-status-changed}",
		groupId = "wager-service"
//		groupId = "${spring.kafka.consumer.group-id}"
	)
	public void consume(String payload) {
	    try {
	        MatchStatusChangedEvent event = objectMapper.readValue(payload, MatchStatusChangedEvent.class);
	        
	        switch (event.status()) {
		        case FINISHED -> ticketService.processMatchFinished(event);
		        case CANCELLED ->ticketService.processMatchCancelled(event);
		        default -> { System.out.println("IN PROGRESS..."); }
	    	}	
	
	    } catch (Exception e) {
	    	// TODO exception kafka consumer
	    }
	}
    
}
