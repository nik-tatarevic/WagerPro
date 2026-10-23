package com.wagerpro.userservice.domain.validation;

import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.wagerpro.userservice.domain.exception.InvalidUserDataException;

@Component
public class UserValidation {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z0-9._-]{3,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    public String normalizeAndValidateUsername(String username) {
        if (username == null) {
            throw new InvalidUserDataException("Username is required.");
        }
        // TODO cekiraj Locale.ROOT
        String normalized = username.strip().toLowerCase(Locale.ROOT);
        if (!USERNAME_PATTERN.matcher(normalized).matches()) {
            throw new InvalidUserDataException(
                    "Username must contain 3-50 letters, digits, dots, underscores or hyphens.");
        }
        
        return normalized;
    }

    public String normalizeAndValidateEmail(String email) {
        if (email == null) {
            throw new InvalidUserDataException("Email is required.");
        }
        String normalized = email.strip().toLowerCase(Locale.ROOT);
        if (normalized.length() > 100 || !EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new InvalidUserDataException("A valid email address of at most 100 characters is required.");
        }
        
        return normalized;
    }

    public String normalizeAndValidateIdentifier(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new InvalidUserDataException("Username or email is required.");
        }
        
        return identifier.strip().toLowerCase(Locale.ROOT);
    }

    public void validatePassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 72) {
            throw new InvalidUserDataException("Password must contain 8-72 characters.");
        }
    }
    
    // TODO napravi validaciju gadjanja starog i novog passworda, ako stignes
}
