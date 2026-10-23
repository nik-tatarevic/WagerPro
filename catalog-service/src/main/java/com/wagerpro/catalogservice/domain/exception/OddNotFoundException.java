package com.wagerpro.catalogservice.domain.exception;

public class OddNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -5894270783374225323L;

	public OddNotFoundException(String message) {
        super(message);
    }
}