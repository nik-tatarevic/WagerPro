package com.wagerpro.wagerservice.domain.exception;

import java.util.List;

import com.wagerpro.wagerservice.infrastructure.rest.dto.ChangedOddsResponseDto;

public class OddsChangedException extends RuntimeException {
	private static final long serialVersionUID = -5813466041503510418L;

	private final List<ChangedOddsResponseDto> response;

    public OddsChangedException(List<ChangedOddsResponseDto> response) {
        super("Odds have changed.");
        this.response = response;
    }

    public List<ChangedOddsResponseDto> getResponse() {
        return response;
    }

}