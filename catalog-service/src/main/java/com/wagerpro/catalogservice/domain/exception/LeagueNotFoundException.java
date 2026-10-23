package com.wagerpro.catalogservice.domain.exception;

public class LeagueNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 151239369143978907L;

	public LeagueNotFoundException(String message) {
        super(message);
    }
}