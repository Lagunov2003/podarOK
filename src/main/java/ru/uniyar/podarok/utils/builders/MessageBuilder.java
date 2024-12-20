package ru.uniyar.podarok.utils.builders;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.entities.Message;
import ru.uniyar.podarok.entities.User;

import java.time.LocalDateTime;

/**
 * Строитель для создания объектов {@link Message}.
 */
@Component
public class MessageBuilder {
    private User sender;
    private User receiver;
    private LocalDateTime timestamp;
    private String content;
    private Boolean read;

    /**
     * Устанавливает отправителя сообщения.
     *
     * @param sender объект пользователя, который отправляет сообщение
     * @return текущий объект {@link MessageBuilder} для дальнейшей настройки
     */
    public MessageBuilder setSender(User sender) {
        this.sender = sender;
        return this;
    }

    /**
     * Устанавливает получателя сообщения.
     *
     * @param receiver объект пользователя, который получает сообщение
     * @return текущий объект {@link MessageBuilder} для дальнейшей настройки
     */
    public MessageBuilder setReceiver(User receiver) {
        this.receiver = receiver;
        return this;
    }

    /**
     * Устанавливает временную метку отправки сообщения.
     *
     * @param timestamp дата и время отправки сообщения
     * @return текущий объект {@link MessageBuilder} для дальнейшей настройки
     */
    public MessageBuilder setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Устанавливает содержание сообщения.
     *
     * @param content текстовое содержимое сообщения
     * @return текущий объект {@link MessageBuilder} для дальнейшей настройки
     */
    public MessageBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Устанавливает статус прочтения сообщения.
     *
     * @param read значение {@code true}, если сообщение прочитано, {@code false} — если нет
     * @return текущий объект {@link MessageBuilder} для дальнейшей настройки
     */
    public MessageBuilder setRead(Boolean read) {
        this.read = read;
        return this;
    }

    /**
     * Строит объект {@link Message} с заданными параметрами.
     *
     * @return новый объект {@link Message}, содержащий все параметры, заданные через методы билдера
     */
    public Message build() {
        Message message = new Message();
        message.setSender(this.sender);
        message.setReceiver(this.receiver);
        message.setTimestamp(this.timestamp);
        message.setContent(this.content);
        message.setRead(this.read);
        return message;
    }
}
