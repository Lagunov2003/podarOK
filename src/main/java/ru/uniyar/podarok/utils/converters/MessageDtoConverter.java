package ru.uniyar.podarok.utils.converters;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.dtos.MessageDto;
import ru.uniyar.podarok.entities.Message;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.utils.builders.MessageBuilder;

import java.time.LocalDateTime;

/**
 * Конвертер для преобразования объектов типа Message в DTO.
 */
@Component
public class MessageDtoConverter {
    /**
     * Преобразует данные в объект Message.
     *
     * @param content текст сообщения.
     * @param sender отправитель сообщения.
     * @param receiver получатель сообщения.
     * @return объект Message, представляющий новое сообщение.
     */
    public Message convertToMessage(String content, User sender, User receiver) {
        return new MessageBuilder()
                .setReceiver(receiver)
                .setSender(sender)
                .setTimestamp(LocalDateTime.now())
                .setContent(content)
                .setRead(false)
                .build();
    }

    /**
     * Преобразует объект Message в MessageDto.
     *
     * @param message объект Message, который необходимо преобразовать.
     * @return объект MessageDto, представляющий данные сообщения в виде DTO.
     */
    public MessageDto convertToMessageDto(Message message) {
        return new MessageDto(message.getContent(), message.getReceiver().getEmail());
    }
}
