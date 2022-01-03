package com.coin.coin.services;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;
import com.coin.coin.models.CoinInfo;
import com.coin.coin.repos.CoinInfoRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    //@Scheduled(fixedDelayString = "PT1M")
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
}
