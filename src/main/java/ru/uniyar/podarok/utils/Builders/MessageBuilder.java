package ru.uniyar.podarok.utils.Builders;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.entities.Message;
import ru.uniyar.podarok.entities.User;

import java.time.LocalDateTime;

@Component
public class MessageBuilder {
    private User sender;
    private User receiver;
    private LocalDateTime timestamp;
    private String content;
    private Boolean read;

    public MessageBuilder setSender(User sender) {
        this.sender = sender;
        return this;
    }

    public MessageBuilder setReceiver(User receiver) {
        this.receiver = receiver;
        return this;
    }

    public MessageBuilder setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public MessageBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public MessageBuilder setRead(Boolean read) {
        this.read = read;
        return this;
    }

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
