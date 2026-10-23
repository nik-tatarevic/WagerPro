package com.wagerpro.wagerservice.infrastructure.evaluator;

import org.springframework.stereotype.Component;

import com.wagerpro.wagerservice.domain.strategy.MarketEvaluatorStrategy;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.MarketType;

@Component
public class Ft1MarketEvaluator implements MarketEvaluatorStrategy {

    @Override
    public MarketType supports() {
        return MarketType.FT_1;
    }

    @Override
    public boolean evaluate(Integer homeGoals, Integer awayGoals) {
        return homeGoals > awayGoals;
    }
}
