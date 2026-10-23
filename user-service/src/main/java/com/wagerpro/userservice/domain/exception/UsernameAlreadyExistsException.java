package com.wagerpro.userservice.domain.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = -5229523212408035979L;

	public UsernameAlreadyExistsException(String username) {
        super("Username '" + username + "' is already in use.");
    }
}
