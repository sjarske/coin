package com.coin.coin.services;

import com.coin.coin.models.TradeRule;
import com.coin.coin.repos.TradeRuleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TradeRuleServiceImpl implements TradeRuleService{

    private final TradeRuleRepo tradeRuleRepo;

    @Override
    public TradeRule saveTradeRule(TradeRule tradeRule) {
        return tradeRuleRepo.save(tradeRule);
    }

    @Override
    public List<TradeRule> getTradeRules() {
        return tradeRuleRepo.findAll();
    }

    @Override
    public TradeRule getTradeRule(String name) {
        return tradeRuleRepo.findByName(name);
    }
}
