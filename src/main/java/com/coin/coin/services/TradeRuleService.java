package com.coin.coin.services;


import com.coin.coin.models.TradeRule;

import java.util.List;

public interface TradeRuleService {
    TradeRule saveTradeRule(TradeRule tradeRule);
    List<TradeRule> getTradeRules();
    TradeRule getTradeRule(String name);
}
