package com.wagerpro.userservice.infrastructure.kafka.producer;

import com.wagerpro.userservice.application.port.UserRegisteredPublisher;
import com.wagerpro.userservice.domain.model.UserDomain;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app.kafka", name = "enabled", havingValue = "false", matchIfMissing = true)
public class NoOpUserRegisteredPublisher implements UserRegisteredPublisher {

    @Override
    public void publish(UserDomain user) {
        // Kafka publishing is deliberately disabled for local development unless KAFKA_ENABLED=true.
    }
}
