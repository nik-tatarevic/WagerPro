package com.wagerpro.catalogservice.domain.model;

import java.time.LocalDateTime;

public record OutboxEventDomain(
		String aggregateId,
		String eventType,
		String payload,
		LocalDateTime createdAt,
		boolean processed
) {}
