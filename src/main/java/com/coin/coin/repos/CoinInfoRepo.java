package com.coin.coin.repos;

import com.coin.coin.models.CoinInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinInfoRepo extends JpaRepository<CoinInfo,Long> {
    CoinInfo findBySymbol(String symbol);
}
