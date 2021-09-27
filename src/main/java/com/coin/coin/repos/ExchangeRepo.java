package com.coin.coin.repos;

import com.coin.coin.models.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRepo extends JpaRepository<Exchange,Long> {
    Exchange findByName(String name);
}
