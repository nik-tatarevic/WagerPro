package com.wagerpro.userservice.infrastructure.kafka.producer;

import com.wagerpro.userservice.application.port.UserRegisteredPublisher;
import com.wagerpro.userservice.domain.model.UserDomain;
import com.wagerpro.userservice.infrastructure.kafka.event.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Component
@ConditionalOnProperty(prefix = "app.kafka", name = "enabled", havingValue = "true")
public class UserRegisteredProducer implements UserRegisteredPublisher {

    private static final Logger log = LoggerFactory.getLogger(UserRegisteredProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public UserRegisteredProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            @Value("${app.kafka.topics.user-registered:wagerpro.user-registered.v1}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    @Override
    public void publish(UserDomain user) {
        Runnable publishAfterCommit = () -> sendEvent(user);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishAfterCommit.run();
                }
            });
        } else {
            publishAfterCommit.run();
        }
    }

    private void sendEvent(UserDomain user) {
        UserRegisteredEvent event = new UserRegisteredEvent(
                user.id(), user.username(), user.email(), Instant.now().toString());
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, user.id().toString(), payload)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            log.error("Could not publish UserRegisteredEvent for userId={}", user.id(), exception);
                        } else {
                            log.debug("Published UserRegisteredEvent for userId={} to topic={}", user.id(), topic);
                        }
                    });
        } catch (RuntimeException exception) {
            // Registration has already committed; a broker outage must not turn it into a misleading HTTP 500.
        	// TODO add exception
            log.error("Could not serialize or send UserRegisteredEvent for userId={}", user.id(), exception);
        }
    }
}
