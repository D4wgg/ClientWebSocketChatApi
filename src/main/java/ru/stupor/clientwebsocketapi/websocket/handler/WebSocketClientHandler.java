package ru.stupor.clientwebsocketapi.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import ru.stupor.clientwebsocketapi.websocket.model.Message;

import java.lang.reflect.Type;

@Component
@Slf4j
public class WebSocketClientHandler extends StompSessionHandlerAdapter {

    @Value("${websocket-server-chat.subscribe}")
    private String websocketServerChatSubscribe;

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("New session established : " + session.getSessionId());
        session.subscribe(websocketServerChatSubscribe, this);
        log.info("Subscribed to " + websocketServerChatSubscribe);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Message.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Message msg = (Message) payload;
        System.out.println(String.format("%s: %s", msg.from(), msg.text()));
    }

}
