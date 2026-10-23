package com.wagerpro.wagerservice.domain.exception;

public class TicketStatusException extends RuntimeException {
	private static final long serialVersionUID = -3558628265744397732L;

	public TicketStatusException(String message) {
	    super(message);
	}

}