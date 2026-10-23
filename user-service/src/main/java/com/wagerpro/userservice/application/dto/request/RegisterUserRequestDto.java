package com.wagerpro.userservice.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequestDto(

		@NotBlank(message = "Username is required")
        @Pattern(regexp = "^[A-Za-z0-9._-]{3,50}$",
                message = "Username must contain 3-50 letters, digits, dots, underscores or hyphens")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must contain 8-72 characters")
        String password

	) {}
