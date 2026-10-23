package com.wagerpro.userservice.domain.exception;

public class EmailAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 9173907932229134265L;

    public EmailAlreadyExistsException(String email) {
        super("Email '" + email + "' is already in use.");
    }
}
