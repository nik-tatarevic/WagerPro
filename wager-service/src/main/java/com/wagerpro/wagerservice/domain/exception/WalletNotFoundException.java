package com.wagerpro.wagerservice.domain.exception;

public class WalletNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 618744803697788595L;

	public WalletNotFoundException(String message) {
	    super(message);
	}

}