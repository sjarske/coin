package com.coin.coin.services;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.binance.api.client.domain.market.TickerStatistics;
import com.coin.coin.models.TradeRule;
import com.coin.coin.models.ws.Greeting;
import com.coin.coin.repos.TradeRuleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.binance.api.client.domain.account.NewOrder.marketBuy;
import static com.binance.api.client.domain.account.NewOrder.marketSell;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TradeRuleServiceImpl implements TradeRuleService{
    private final TradeRuleRepo tradeRuleRepo;

    @Autowired
    SimpMessagingTemplate template;

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

    public void send(String message) {
        template.convertAndSend("/topic/greetings", new Greeting(message));
    }


    @Override
    @Scheduled(fixedDelay = 10000L)
    public void checkTradeRules() {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("aoENyRgtqNkeH5FVFqDqB0QoU4r6OR6XN187tI0KuwE2JZTWBaM4vYYwaU6nuX9p", "Ni7aHTlCMTht0qhxeUakmdnYc5WifkjQxJpBnidYwLapovcXvZ99qQm7gNLbsgaW",true,true);
        BinanceApiRestClient client = factory.newRestClient();

        TickerStatistics tickerStatistics = client.get24HrPriceStatistics("BTCUSDT");

        List<TradeRule> tradeRules = tradeRuleRepo.findAll();
        for (TradeRule rule : tradeRules)
        {
            if (!rule.isCompleted()){
                double targetValue = rule.getCoinValueOnCreate() * rule.getIfPercentage()/100;
                double currentValue = Double.parseDouble(tickerStatistics.getLastPrice());

                if (rule.getCoinCondition().equals("increased by"))
                {

                    if (currentValue >= targetValue){
                        send("Condition for '"+rule.getName() +"' is met");
                        //System.out.println("Condition for '"+rule.getName() +"' is met");
                        send("Current value: "+currentValue+" -- Target value: "+targetValue);
                        //System.out.println("Current value: "+currentValue+" -- Target value: "+targetValue);
                        send("Selling...");
                        //System.out.println("Selling...");
                        NewOrderResponse newOrderResponse = client.newOrder(marketSell("BTCUSDT", "0.001").newOrderRespType(NewOrderResponseType.FULL));
                        send(newOrderResponse.getClientOrderId());
                        //System.out.println(newOrderResponse.getClientOrderId());
                        rule.setCompleted(true);

                    }else
                    {
                        send("Condition for '"+rule.getName() +"' is not met");
                        //System.out.println("Condition for '"+rule.getName() +"' is not met");
                        send("Current value: "+currentValue+" -- Target value: "+targetValue);
                        //System.out.println("Current value: "+currentValue+" -- Target value: "+targetValue);
                    }
                }

                if (rule.getCoinCondition().equals("decreased by"))
                {
                    if (currentValue <= targetValue){
                        send("Condition for '"+rule.getName() +"' is met");
                        //System.out.println("Condition for '"+rule.getName() +"' is met");
                        send("Current value: "+currentValue+" -- Target value: "+targetValue);
                        // System.out.println("Current value: "+currentValue+" -- Target value: "+targetValue);
                        send("Buying...");
                        //System.out.println("Buying...");

                        NewOrderResponse newOrderResponse = client.newOrder(marketBuy("BTCUSDT", "0.001").newOrderRespType(NewOrderResponseType.FULL));
                        send(newOrderResponse.getClientOrderId());
                        //System.out.println(newOrderResponse.getClientOrderId());
                        rule.setCompleted(true);

                    }else
                    {
                        send("Condition for '"+rule.getName() +"' is not met");
                        //System.out.println("Condition for '"+rule.getName() +"' is not met");
                        send("Current value: "+currentValue+" -- Target value: "+targetValue);
                        //System.out.println("Current value: "+currentValue+" -- Target value: "+targetValue);
                    }
                }
            }
        }
        send("No further updates");
        //System.out.println("No further updates");
    }
}
