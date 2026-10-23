package com.wagerpro.userservice.domain.exception;

public class InvalidCredentialsException extends RuntimeException {
    private static final long serialVersionUID = -2052509745314475244L;

	public InvalidCredentialsException() {
        super("Invalid username/email or password.");
    }
}
