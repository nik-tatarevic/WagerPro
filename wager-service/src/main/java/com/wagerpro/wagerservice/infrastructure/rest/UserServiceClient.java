package com.wagerpro.wagerservice.infrastructure.rest;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.wagerpro.wagerservice.application.dto.response.MatchDetailsDto;
import com.wagerpro.wagerservice.infrastructure.rest.dto.UserResponseDto;

public interface UserServiceClient {

	@GetExchange("/users/{userId}")
	UserResponseDto getUserById(@PathVariable Long userId);
	
}
