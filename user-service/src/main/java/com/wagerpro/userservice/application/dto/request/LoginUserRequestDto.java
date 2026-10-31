package com.wagerpro.userservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUserRequestDto(

		@NotBlank(message = "Username or email is required")
        @Size(max = 100, message = "Username or email must not exceed 100 characters")
        String identifier,

        @NotBlank(message = "Password is required")
        @Size(max = 72, message = "Password must not exceed 72 characters")
        String password
		
) {}
