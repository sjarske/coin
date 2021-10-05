package com.coin.coin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CoinApplicationTests {

	@Test
	void contextLoads() {
		assertThat(1).isEqualTo(1);
	}

}
