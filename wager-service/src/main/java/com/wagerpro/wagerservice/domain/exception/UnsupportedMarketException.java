package com.wagerpro.wagerservice.domain.exception;

public class UnsupportedMarketException extends RuntimeException {
	private static final long serialVersionUID = 6737513257833522228L;

	public UnsupportedMarketException(String message) {
	    super(message);
	}

}