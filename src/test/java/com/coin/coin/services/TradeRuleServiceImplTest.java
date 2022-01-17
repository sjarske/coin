package com.coin.coin.services;

import com.coin.coin.models.TradeRule;
import com.coin.coin.models.User;
import com.coin.coin.repos.TradeRuleRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class TradeRuleServiceImplTest {

    @Mock
    private TradeRuleRepo tradeRuleRepo;

    private TradeRuleServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new TradeRuleServiceImpl(tradeRuleRepo);
    }

    @Test
    void saveTradeRule() {
        TradeRule tradeRule = new TradeRule(null,"testrule",100,"btc","price","1",100,100,"sell",false);
        underTest.saveTradeRule(tradeRule);
        ArgumentCaptor<TradeRule> tradeRuleArgumentCaptor = ArgumentCaptor.forClass(TradeRule.class);
        verify(tradeRuleRepo).save(tradeRuleArgumentCaptor.capture());
        TradeRule capturedTR = tradeRuleArgumentCaptor.getValue();
        assertThat(capturedTR).isEqualTo(tradeRule);
    }

    @Test
    void getTradeRules() {
        underTest.getTradeRules();
        verify(tradeRuleRepo).findAll();
    }

    @Test
    void getTradeRule() {
        TradeRule tradeRule = new TradeRule(null,"testrule",100,"btc","price","1",100,100,"sell",false);
        underTest.saveTradeRule(tradeRule);
        underTest.getTradeRule(tradeRule.getName());
        verify(tradeRuleRepo).findByName("testrule");
    }
}