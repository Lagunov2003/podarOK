package ru.uniyar.podarok.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.uniyar.podarok.entities.Message;

import java.time.LocalDateTime;

/**
 * Контроллер для работы с WebSocket сообщениями в чате.
 * Обрабатывает входящие сообщения и отправляет их всем подписанным пользователям.
 */
@Controller
public class WebSocketChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        return message;
    }
}
