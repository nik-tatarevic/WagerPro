package com.wagerpro.wagerservice.infrastructure.rest;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import com.wagerpro.wagerservice.application.dto.ItemOddDetailsDto;
import com.wagerpro.wagerservice.application.dto.response.MatchDetailsDto;
import com.wagerpro.wagerservice.infrastructure.rest.dto.OddResponseDto;

public interface CatalogServiceClient {
	
	@PostExchange("/odds/check-ticket-odds")
	List<OddResponseDto> checkTicketOdds(@RequestBody List<ItemOddDetailsDto> oddsValueRequestDtos);
	
	@PostExchange("/matches/ticket-summary")
	List<MatchDetailsDto> findDetailsForTicket(@RequestBody Set<Long> matchIds);
}
