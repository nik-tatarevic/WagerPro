package com.wagerpro.wagerservice.domain.strategy;

import java.util.List;

import com.wagerpro.wagerservice.domain.model.TicketItemDomain;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.TicketType;

public interface BetEvaluatorStrategy {
	
	TicketType supports();

    boolean evaluate(List<TicketItemDomain> ticketItemDomains);
}
