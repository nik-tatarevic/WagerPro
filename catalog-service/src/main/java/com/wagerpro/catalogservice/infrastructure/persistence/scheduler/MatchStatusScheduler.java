package com.wagerpro.catalogservice.infrastructure.persistence.scheduler;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wagerpro.catalogservice.infrastructure.persistence.entity.OutboxEvent;
import com.wagerpro.catalogservice.infrastructure.persistence.repository.OutboxEventJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchStatusScheduler {
	
	private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxEventJpaRepository outboxRepository;
    
    @Value("${app.kafka.topic.match-status-changed}")
    private String topic;

    @Scheduled(fixedDelay = 15000)
    public void processOutboxEvents() {
        List<OutboxEvent> events = outboxRepository.findByProcessedFalse();

        for (OutboxEvent event : events) {
            kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload())
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            event.setProcessed(true);
                            outboxRepository.save(event);
                        }
                        
                        // LOG error sending event
                    });
        }
    }

}
