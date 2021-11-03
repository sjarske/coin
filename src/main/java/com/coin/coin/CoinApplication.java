package com.coin.coin;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.coin.coin.models.Role;
import com.coin.coin.models.TradeRule;
import com.coin.coin.models.User;
import com.coin.coin.services.TradeRuleService;
import com.coin.coin.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class CoinApplication {

	public static void main(String[] args) {

		SpringApplication.run(CoinApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner runner(UserService userService, TradeRuleService tradeRuleService){
		return args -> {
			userService.saveRole(new Role(null,"ROLE_USER"));
			userService.saveRole(new Role(null,"ROLE_ADMIN"));

			userService.saveUser(new User(null,"John","John","1234", new ArrayList<>(),null));
			userService.saveUser(new User(null,"jane","Jane","1234", new ArrayList<>(),null));

			userService.addRoleToUser("Jane","ROLE_USER");
			userService.addRoleToUser("John","ROLE_ADMIN");

			tradeRuleService.saveTradeRule(new TradeRule(null,"sell with profit","BTC","price","increased by",5,100,"sell"));
			tradeRuleService.saveTradeRule(new TradeRule(null,"buy in dip","BTC","price","decreased by",20,30,"buy"));

		};
	}


}
