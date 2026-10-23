package com.wagerpro.userservice.domain.strategy;

public interface PasswordEncoderStrategy {
	String encode(String rawPassword);
    boolean matches(String rawPassword, String encodedPassword);
}
