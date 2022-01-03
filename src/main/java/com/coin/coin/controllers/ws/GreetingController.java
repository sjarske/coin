package com.coin.coin.controllers.ws;

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
        return new Greeting(HtmlUtils.htmlEscape(message.getName()));
    }

}
