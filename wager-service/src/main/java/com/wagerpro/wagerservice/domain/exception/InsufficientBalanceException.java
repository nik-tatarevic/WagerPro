package com.wagerpro.wagerservice.domain.exception;

public class InsufficientBalanceException extends RuntimeException {
	private static final long serialVersionUID = 5007149172965349607L;

	public InsufficientBalanceException(String message) {
	    super(message);
	}

}