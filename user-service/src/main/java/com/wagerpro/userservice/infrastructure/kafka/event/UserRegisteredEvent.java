package com.wagerpro.userservice.infrastructure.kafka.event;

public record UserRegisteredEvent(
        Long userId,
        String username,
        String email,
        String occurredAt) {
}
