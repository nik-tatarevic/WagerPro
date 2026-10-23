package com.wagerpro.wagerservice.application.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wagerpro.wagerservice.domain.strategy.MarketEvaluatorStrategy;
import com.wagerpro.wagerservice.infrastructure.persistence.enums.MarketType;

@Service
public class MarketEvaluatorService {

    private final Map<MarketType, MarketEvaluatorStrategy> evaluators;

    public MarketEvaluatorService(List<MarketEvaluatorStrategy> strategies) {
        this.evaluators = Map.copyOf(
                strategies.stream()
                        .collect(Collectors.toMap(
                                MarketEvaluatorStrategy::supports,
                                Function.identity()
                        ))
        );
    }

    public MarketEvaluatorStrategy getStrategy(MarketType marketType) {
        return Optional.ofNullable(evaluators.get(marketType))
                .orElseThrow(); 
//        .orElseThrow(() -> new UnsupportedMarketException(type)); TODO
    }

}
