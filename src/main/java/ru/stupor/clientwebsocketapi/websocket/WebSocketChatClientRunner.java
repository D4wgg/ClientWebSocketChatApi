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

    private static final String EXIT_COMMAND = "/exit";

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

        System.out.print("Input your name: ");
        var senderName = scanner.nextLine();

        while (senderName.isBlank()) {
            System.out.println("The name can't be blank!");
            System.out.print("Input your name: ");
            senderName = scanner.nextLine();
        }

        System.out.println("Your name is " + senderName);
        System.out.println(String.format("Type '%s' to leave chat", EXIT_COMMAND));

        System.out.println("Chat: ");
        var input = scanner.nextLine();

        while(!input.equals(EXIT_COMMAND)) {
            var message = Message.builder()
                    .from(senderName)
                    .text(input)
                    .build();
            session.send(websocketServerChatSend, message);
            input = scanner.nextLine();
        }

        System.exit(0);
    }
}
