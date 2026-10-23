package com.wagerpro.catalogservice.domain.exception;

public class InvalidMatchStatusException extends RuntimeException {
	private static final long serialVersionUID = -5894270783374225323L;

	public InvalidMatchStatusException(String message) {
        super(message);
    }
}