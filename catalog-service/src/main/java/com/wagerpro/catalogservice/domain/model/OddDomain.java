package com.wagerpro.catalogservice.domain.model;

import java.math.BigDecimal;

import com.wagerpro.catalogservice.domain.exception.InvalidOddValueException;

public record OddDomain(
		Long id, 
		Long matchId, 
		Long marketDefinitionId, 
		BigDecimal oddValue, 
		boolean isActive,
		Long version) {
	
	public OddDomain {
		validateOddValue(oddValue);
    }

	public OddDomain changeOdd(BigDecimal newOddValue) {
		return new OddDomain(id, matchId, marketDefinitionId, newOddValue, isActive, version);
	}

	public OddDomain activate() {
		return new OddDomain(id, matchId, marketDefinitionId, oddValue, true, version);
	}

	public OddDomain deactivate() {
		return new OddDomain(id, matchId, marketDefinitionId, oddValue, false, version);
	}
	
	public OddDomain setMarketDefinitionId(Long marketDefinitionId) {
		return new OddDomain(id, matchId, marketDefinitionId, oddValue, true, version);
	}

	private static void validateOddValue(BigDecimal value) {
		if (value == null || value.compareTo(new BigDecimal("1.01")) < 0) {
			throw new InvalidOddValueException("Odd value must be greater or equal than 1.01");
		}
	}
	
}
