package com.coin.coin.controllers.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleWebSocketConnectListener(final SessionConnectedEvent event){
        LOGGER.info("user connected");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(final SessionDisconnectEvent event){
        LOGGER.info("user disconnected");
    }
}
