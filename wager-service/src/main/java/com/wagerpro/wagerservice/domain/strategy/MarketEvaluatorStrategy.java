package com.wagerpro.wagerservice.domain.strategy;

import com.wagerpro.wagerservice.infrastructure.persistence.enums.MarketType;

public interface MarketEvaluatorStrategy {
	
	MarketType supports();

    boolean evaluate(Integer homeGoals, Integer awayGoals);

}
