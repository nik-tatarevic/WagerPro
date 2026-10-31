package com.wagerpro.wagerservice.infrastructure.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import com.wagerpro.wagerservice.infrastructure.rest.dto.UserResponseDto;

public interface UserServiceClient {

	@GetExchange("/users/{userId}")
	UserResponseDto getUserById(@PathVariable Long userId);
	
}
