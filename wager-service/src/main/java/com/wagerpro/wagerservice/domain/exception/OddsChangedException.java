package com.wagerpro.wagerservice.domain.exception;

import java.util.List;

import com.wagerpro.wagerservice.infrastructure.rest.dto.OddResponseDto;

public class OddsChangedException extends RuntimeException {
	private static final long serialVersionUID = -5813466041503510418L;

	private final List<OddResponseDto> response;

    public OddsChangedException(List<OddResponseDto> response) {
        super("Odds have changed.");
        this.response = response;
    }

    public List<OddResponseDto> getResponse() {
        return response;
    }

}