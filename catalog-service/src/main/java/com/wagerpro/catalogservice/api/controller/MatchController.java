package com.wagerpro.catalogservice.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wagerpro.catalogservice.application.dto.request.CreateMatchRequestDto;
import com.wagerpro.catalogservice.application.dto.request.UpdateMatchStatusRequestDto;
import com.wagerpro.catalogservice.application.dto.response.MatchDetailsResponseDto;
import com.wagerpro.catalogservice.application.dto.response.MatchOddsSummaryResponseDto;
import com.wagerpro.catalogservice.application.dto.response.MatchResponseDto;
import com.wagerpro.catalogservice.application.dto.response.UpcomingMatchesResponseDto;
import com.wagerpro.catalogservice.application.service.MatchService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {
	
	private final MatchService matchService;
	
	@PostMapping
	public ResponseEntity<MatchResponseDto> save(@Valid @RequestBody CreateMatchRequestDto matchRequestDto) {
		MatchResponseDto matchResponseDto = matchService.save(matchRequestDto);
		return new ResponseEntity<>(matchResponseDto, HttpStatus.CREATED);
	}
	
	@PatchMapping("/update-status")
	public ResponseEntity<MatchResponseDto> updateMatchStatus(@Valid @RequestBody UpdateMatchStatusRequestDto request) {
		MatchResponseDto match = matchService.updateMatchStatus(request);
		return ResponseEntity.ok(match);
	}
	
	@GetMapping("/upcoming")
	public ResponseEntity<List<UpcomingMatchesResponseDto>> findUpcomingMatchesBySport(@RequestParam String sportName) {
		List<UpcomingMatchesResponseDto> matchesBySportAndHours = matchService.findUpcomingMatchesBySport(sportName);
		return new ResponseEntity<>(matchesBySportAndHours, HttpStatus.OK);
	}
	
	@GetMapping("/upcoming-by-hours")
	public ResponseEntity<List<UpcomingMatchesResponseDto>> findUpcomingMatchesBySportAndHours(
	        @RequestParam String sportName,
	        @RequestParam int hours, 
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    
	    List<UpcomingMatchesResponseDto> matches = 
	    		matchService.findUpcomingMatchesBySportAndHours(sportName, hours, page, size);
	    return ResponseEntity.ok(matches);
	}
	
	@GetMapping("/{matchId}/odds")
	public ResponseEntity<List<MatchOddsSummaryResponseDto>> findOddsByMatchId(@PathVariable Long matchId) {
		List<MatchOddsSummaryResponseDto> matchWithOdds = matchService.findOddsByMatchId(matchId);
		return ResponseEntity.ok(matchWithOdds);
	}
	
	@PostMapping("/ticket-summary")
	public ResponseEntity<List<MatchDetailsResponseDto>> findDetailsForTicket(@RequestBody List<Long> matchIds) {
		List<MatchDetailsResponseDto> matchWithOdds = matchService.findDetailsForTicket(matchIds);
		return ResponseEntity.ok(matchWithOdds);
	}
	
	
}
