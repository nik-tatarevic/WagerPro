package com.wagerpro.userservice.infrastructure.security;

import com.wagerpro.userservice.domain.strategy.PasswordEncoderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordEncoderStrategy implements PasswordEncoderStrategy {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordEncoderStrategy(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
