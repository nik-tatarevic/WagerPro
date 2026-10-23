package com.wagerpro.catalogservice.domain.exception;

public class InvalidOddValueException extends RuntimeException {
	private static final long serialVersionUID = -1689343768175989699L;

	public InvalidOddValueException(String message) {
        super(message);
    }
}