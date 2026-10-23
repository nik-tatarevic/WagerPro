package com.wagerpro.wagerservice.infrastructure.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.wagerpro.wagerservice.application.service.TicketService;
import com.wagerpro.wagerservice.infrastructure.kafka.MatchCancelledEvent;
import com.wagerpro.wagerservice.infrastructure.kafka.MatchFinishedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MatchFinishedConsumer {

	private final TicketService ticketService;

	@KafkaListener(topics = "match-finished")
	public void consume(MatchFinishedEvent event) {
		ticketService.processMatchFinished(event);
	}
	
	@KafkaListener(topics = "match-cancelled")
	public void consume(MatchCancelledEvent event) {
		ticketService.processMatchCancelled(event);
	}
}
