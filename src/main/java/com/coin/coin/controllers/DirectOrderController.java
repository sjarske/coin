package com.coin.coin.controllers;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.NewOrderResponse;
import com.coin.coin.models.BuyDirectForm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.binance.api.client.domain.account.NewOrder.marketBuy;
import static com.binance.api.client.domain.account.NewOrder.marketSell;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DirectOrderController {

    @PostMapping("/directorder/buy")
    public void placeBuyMarketOrder(@RequestBody BuyDirectForm buyDirectFrom){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("aoENyRgtqNkeH5FVFqDqB0QoU4r6OR6XN187tI0KuwE2JZTWBaM4vYYwaU6nuX9p", "Ni7aHTlCMTht0qhxeUakmdnYc5WifkjQxJpBnidYwLapovcXvZ99qQm7gNLbsgaW",true,true);
        BinanceApiRestClient client = factory.newRestClient();

        client.newOrder(marketBuy(buyDirectFrom.getCoin()+"USDT", buyDirectFrom.getAmount()));
    }

    @PostMapping("/directorder/sell")
    public void placeSellMarketOrder(@RequestBody BuyDirectForm buyDirectFrom){
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("aoENyRgtqNkeH5FVFqDqB0QoU4r6OR6XN187tI0KuwE2JZTWBaM4vYYwaU6nuX9p", "Ni7aHTlCMTht0qhxeUakmdnYc5WifkjQxJpBnidYwLapovcXvZ99qQm7gNLbsgaW",true,true);
        BinanceApiRestClient client = factory.newRestClient();

        client.newOrder(marketSell(buyDirectFrom.getCoin()+"USDT", buyDirectFrom.getAmount()));
    }
}
