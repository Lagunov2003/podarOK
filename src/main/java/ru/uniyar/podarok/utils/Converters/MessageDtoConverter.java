package ru.uniyar.podarok.utils.Converters;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.dtos.MessageDto;
import ru.uniyar.podarok.entities.Message;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.utils.Builders.MessageBuilder;

import java.time.LocalDateTime;

/**
 * Конвертер для преобразования объектов типа Message в DTO.
 */
@Component
public class MessageDtoConverter {
    public Message convertToMessage(String content, User sender, User receiver) {
        return new MessageBuilder()
                .setReceiver(receiver)
                .setSender(sender)
                .setTimestamp(LocalDateTime.now())
                .setContent(content)
                .setRead(false)
                .build();
    }

    public MessageDto convertToMessageDto(Message message) {
        return new MessageDto(message.getContent(), message.getReceiver().getEmail());
    }
}
