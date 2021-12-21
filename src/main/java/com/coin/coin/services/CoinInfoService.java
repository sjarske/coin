package com.coin.coin.services;

import com.coin.coin.models.CoinInfo;

import java.util.List;

public interface CoinInfoService {
    void getAndSaveCoinInfo();
    List<CoinInfo> getCoinInfos();
    void checkTradeRules();
}
