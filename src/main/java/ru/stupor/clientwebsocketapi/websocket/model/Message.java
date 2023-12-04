package ru.stupor.clientwebsocketapi.websocket.model;

import lombok.Builder;

@Builder
public record Message(String from, String text) { }
