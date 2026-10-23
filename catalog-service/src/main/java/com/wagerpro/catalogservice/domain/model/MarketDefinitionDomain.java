package com.wagerpro.catalogservice.domain.model;

import com.wagerpro.catalogservice.infrastructure.persistence.enums.MarketDefinitionType;

public record MarketDefinitionDomain(Long id, Long sportId, MarketDefinitionType type, String description) {
}
