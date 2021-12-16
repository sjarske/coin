package com.coin.coin.services;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;
import com.coin.coin.models.CoinInfo;
import com.coin.coin.models.TradeRule;
import com.coin.coin.repos.CoinInfoRepo;
import com.coin.coin.repos.TradeRuleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CoinInfoServiceImpl implements CoinInfoService{
    private final CoinInfoRepo coinInfoRepo;
    private final TradeRuleRepo tradeRuleRepo;

    @Override
    @Scheduled(fixedDelayString = "PT1M")
    public void getAndSaveCoinInfo() {
            BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
            BinanceApiRestClient client = factory.newRestClient();

            TickerStatistics tickerStatistics = client.get24HrPriceStatistics("BTCUSDT");

            CoinInfo coinInfo = new CoinInfo(null,"BTCUSDT",tickerStatistics.getLastPrice(), LocalDateTime.now());
            coinInfoRepo.save(coinInfo);
    }

    @Override
    public List<CoinInfo> getCoinInfos() {
        return coinInfoRepo.findAll();
    }

    @Override
    @Scheduled(fixedDelay = 10000L)
    public String checkTradeRules() {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("aoENyRgtqNkeH5FVFqDqB0QoU4r6OR6XN187tI0KuwE2JZTWBaM4vYYwaU6nuX9p", "Ni7aHTlCMTht0qhxeUakmdnYc5WifkjQxJpBnidYwLapovcXvZ99qQm7gNLbsgaW",true,true);
        BinanceApiRestClient client = factory.newRestClient();

        TickerStatistics tickerStatistics = client.get24HrPriceStatistics("BTCUSDT");

        List<TradeRule> tradeRules = tradeRuleRepo.findAll();
        for (TradeRule rule : tradeRules)
        {
            if (rule.getCoinCondition().equals("increased by"))
            {
                int target = rule.getCoinValueOnCreate() % 100 * (100 + rule.getIfPercentage());


            }

            if (rule.getCoinCondition()=="decreased by")
            {

            }

        }
        return "no updates";
    }


}
