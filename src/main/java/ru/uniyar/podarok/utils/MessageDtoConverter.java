package ru.uniyar.podarok.utils;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.dtos.MessageDto;
import ru.uniyar.podarok.entities.Message;
import ru.uniyar.podarok.entities.User;

import java.time.LocalDateTime;

/**
 * Конвертер для преобразования объектов типа Message в DTO.
 */
@Component
public class MessageDtoConverter {
    public Message convertToMessage(String content, User sender, User receiver) {
        Message message = new Message();
        message.setReceiver(receiver);
        message.setSender(sender);
        message.setTimestamp(LocalDateTime.now());
        message.setContent(content);
        message.setRead(false);
        return message;
    }

    public MessageDto convertToMessageDto(Message message) {
        return new MessageDto(message.getContent(), message.getReceiver().getEmail());
    }
}
