package com.coin.coin.controllers.ws;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.TickerStatistics;
import com.coin.coin.models.ws.Greeting;
import com.coin.coin.models.ws.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Message message) {
        if (message.getName().contains("pair:")){
            String content = message.getName().replaceFirst("pair:", "");
            System.out.println(content);
            BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
            BinanceApiRestClient client = factory.newRestClient();

            TickerStatistics tickerStatistics = client.get24HrPriceStatistics(content);
            message.setName(tickerStatistics.getLastPrice());
        }
        return new Greeting(HtmlUtils.htmlEscape(message.getName()));
    }

}
