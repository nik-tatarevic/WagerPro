package com.wagerpro.catalogservice.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagerpro.catalogservice.application.dto.ChangedOddsResponseDto;
import com.wagerpro.catalogservice.application.dto.OddsValueRequestDto;
import com.wagerpro.catalogservice.application.dto.request.CreateOrUpdateOddRequestDto;
import com.wagerpro.catalogservice.application.dto.response.OddResponseDto;
import com.wagerpro.catalogservice.application.service.OddService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/odds")
public class OddController {
	
	private final OddService oddService;
	
	@PostMapping
	public ResponseEntity<OddResponseDto> saveOdd(@Valid @RequestBody CreateOrUpdateOddRequestDto request) {
		OddResponseDto savedOdd = oddService.saveOdd(request);
		return new ResponseEntity<>(savedOdd, HttpStatus.CREATED);
	}
	
	@PatchMapping
	public ResponseEntity<OddResponseDto> updateOdd(@Valid @RequestBody CreateOrUpdateOddRequestDto request) {
		OddResponseDto savedOdd = oddService.updateOdd(request);
		return new ResponseEntity<>(savedOdd, HttpStatus.CREATED);
	}
	
	@PostMapping("/check-ticket-odds")
	public ResponseEntity<List<ChangedOddsResponseDto>> checkTicketOdds(@Valid @RequestBody List<OddsValueRequestDto> request) {
		List<ChangedOddsResponseDto> savedOdd = oddService.checkTicketOdds(request);
		return new ResponseEntity<>(savedOdd, HttpStatus.OK);
	}

	// DEACTIVETE() 
	// ACTIVATE()
	
}
