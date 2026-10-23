package com.wagerpro.wagerservice.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wagerpro.wagerservice.application.dto.request.CreateTicketRequestDto;
import com.wagerpro.wagerservice.application.dto.response.TicketBasicResponseDto;
import com.wagerpro.wagerservice.application.dto.response.TicketDetailsResponseDto;
import com.wagerpro.wagerservice.application.service.TicketService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {
	
	private final TicketService ticketService;
	
	@PostMapping
	public ResponseEntity<TicketBasicResponseDto> createTicket(@Valid @RequestBody CreateTicketRequestDto request) {
		TicketBasicResponseDto ticket = ticketService.createTicket(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ticket);
	}
	
	@GetMapping
	public ResponseEntity<Page<TicketBasicResponseDto>> findAllByUser(
			@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<TicketBasicResponseDto> tickets = ticketService.findAllByUser(pageable);
		return ResponseEntity.ok(tickets);
	}
	
	@GetMapping("/{ticketId}")
	public ResponseEntity<TicketDetailsResponseDto> findTicketDetailsById(@PathVariable Long ticketId) {
		TicketDetailsResponseDto ticket = ticketService.findTicketDetailsById(ticketId);
		return ResponseEntity.ok(ticket);
	}

}
