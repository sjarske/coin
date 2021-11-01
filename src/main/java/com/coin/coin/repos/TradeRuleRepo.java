package com.coin.coin.repos;

import com.coin.coin.models.TradeRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRuleRepo extends JpaRepository<TradeRule, Long> {
    TradeRule findByName(String name);
}
