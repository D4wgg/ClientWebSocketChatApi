package ru.stupor.clientwebsocketapi.websocket;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.stupor.clientwebsocketapi.websocket.handler.WebSocketClientHandler;
import ru.stupor.clientwebsocketapi.websocket.model.Message;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class WebSocketChatClientRunner implements CommandLineRunner {

    private final WebSocketClientHandler webSocketClientHandler;

    @Value("${websocket-server-chat.url}")
    private String websocketServerUrl;
    @Value("${websocket-server-chat.send}")
    private String websocketServerChatSend;


    @Override
    @SneakyThrows
    public void run(String... args) {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSession session = stompClient.connectAsync(websocketServerUrl, webSocketClientHandler).get();

        var scanner = new Scanner(System.in);
        var input = scanner.nextLine();

        while(!input.equals("exit")) {
            var message = new Message();
            message.setFrom("Ivan");
            message.setText(input);
            session.send(websocketServerChatSend, message);
            input = scanner.nextLine();
        }
    }
}
