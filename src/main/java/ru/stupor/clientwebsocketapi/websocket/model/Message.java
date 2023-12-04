package ru.stupor.clientwebsocketapi.websocket.model;

import lombok.Data;

@Data
public class Message {

    private String from;
    private String text;

}
