package com.wagerpro.wagerservice.domain.exception;

public class AmountException extends RuntimeException {
	private static final long serialVersionUID = 4035240243713365699L;

	public AmountException(String message) {
	    super(message);
	}

}