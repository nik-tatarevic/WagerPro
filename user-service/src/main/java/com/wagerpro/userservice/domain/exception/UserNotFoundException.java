package com.wagerpro.userservice.domain.exception;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1601355251532480459L;

	public UserNotFoundException(String message) {
        super(message);
    }
}
