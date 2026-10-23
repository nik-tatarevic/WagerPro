package com.wagerpro.wagerservice.domain.exception;

public class WalletBlockedException extends RuntimeException {
	private static final long serialVersionUID = 353578003177929572L;

	public WalletBlockedException(String message) {
	    super(message);
	}

}