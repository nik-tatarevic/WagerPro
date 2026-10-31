package com.wagerpro.catalogservice.domain.validation;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.wagerpro.catalogservice.domain.exception.InvalidMatchStartTime;

@Component
public class MatchValidation {
	
	public void validateStartTime(LocalDateTime startTime) { 
		if (startTime.compareTo(LocalDateTime.now()) <= 0) {
			throw new InvalidMatchStartTime("The date and time must be in the future.");
		}
	}
	
}
