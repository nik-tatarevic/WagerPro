package com.wagerpro.catalogservice.application.dto;

public record CreateStatusChangedOutBoxDto(
		String aggregateId, 
		String payload
) {}
