package com.wagerpro.catalogservice.domain.exception;

public class MatchNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 2080940826822406043L;

	public MatchNotFoundException(String message) {
        super(message);
    }
}