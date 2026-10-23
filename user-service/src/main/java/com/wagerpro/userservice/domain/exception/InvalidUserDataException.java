package com.wagerpro.userservice.domain.exception;

public class InvalidUserDataException extends RuntimeException {
    private static final long serialVersionUID = -2021213321578451112L;

	public InvalidUserDataException(String message) {
        super(message);
    }
}
