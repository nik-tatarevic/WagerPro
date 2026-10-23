package com.wagerpro.wagerservice.domain.exception;

public class TicketNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 9208109350953473336L;

	public TicketNotFoundException(String message) {
	    super(message);
	}

}