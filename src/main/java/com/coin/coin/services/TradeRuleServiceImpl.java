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

    private String btcusdt= "BTCUSDT";
    private String condition= "Condition for '";
    private String target =" -- Target value: ";
    private String current = "Current value: ";



    @Override
    @Scheduled(fixedDelay = 10000L)
    public void checkTradeRules() {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("aoENyRgtqNkeH5FVFqDqB0QoU4r6OR6XN187tI0KuwE2JZTWBaM4vYYwaU6nuX9p", "Ni7aHTlCMTht0qhxeUakmdnYc5WifkjQxJpBnidYwLapovcXvZ99qQm7gNLbsgaW",true,true);
        BinanceApiRestClient client = factory.newRestClient();

        TickerStatistics tickerStatistics = client.get24HrPriceStatistics(btcusdt);

        List<TradeRule> tradeRules = tradeRuleRepo.findAll();
        for (TradeRule rule : tradeRules)
        {
            if (!rule.isCompleted()){
                double targetValue = (double)rule.getCoinValueOnCreate() * rule.getIfPercentage()/100;
                double currentValue = Double.parseDouble(tickerStatistics.getLastPrice());

                if (rule.getCoinCondition().equals("increased by"))
                {

                    if (currentValue >= targetValue){
                        send(condition+rule.getName() +"' is met");
                        send(current+currentValue+target+targetValue);
                        send("Selling...");
                        NewOrderResponse newOrderResponse = client.newOrder(marketSell(btcusdt, "0.001").newOrderRespType(NewOrderResponseType.FULL));
                        send(newOrderResponse.getClientOrderId());
                        rule.setCompleted(true);

                    }else
                    {
                        send(condition+rule.getName() +"' is not met");
                        send(current+currentValue+target+targetValue);
                    }
                }

                if (rule.getCoinCondition().equals("decreased by"))
                {
                    if (currentValue <= targetValue){
                        send(condition+rule.getName() +"' is met");
                        send(current+currentValue+target+targetValue);
                        send("Buying...");
                        NewOrderResponse newOrderResponse = client.newOrder(marketBuy(btcusdt, "0.001").newOrderRespType(NewOrderResponseType.FULL));
                        send(newOrderResponse.getClientOrderId());
                        rule.setCompleted(true);

                    }else
                    {
                        send(condition+rule.getName() +"' is not met");
                        send(current+currentValue+target+targetValue);
                    }
                }
            }
        }
        send("No further updates");
        //System.out.println("No further updates");
    }
}
