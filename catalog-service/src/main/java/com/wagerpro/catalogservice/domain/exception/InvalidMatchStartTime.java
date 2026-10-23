package com.wagerpro.catalogservice.domain.exception;

public class InvalidMatchStartTime extends RuntimeException {
	private static final long serialVersionUID = -588139728683478929L;

	public InvalidMatchStartTime(String message) {
        super(message);
    }
}