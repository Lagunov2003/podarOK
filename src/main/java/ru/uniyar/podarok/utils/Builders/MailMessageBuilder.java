package ru.uniyar.podarok.utils.Builders;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class MailMessageBuilder {
    private String from;
    private String[] to;
    private String subject;
    private String text;

    public MailMessageBuilder from(String from) {
        this.from = from;
        return this;
    }

    public MailMessageBuilder to(String... to) {
        this.to = to;
        return this;
    }

    public MailMessageBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public MailMessageBuilder text(String text) {
        this.text = text;
        return this;
    }

    public SimpleMailMessage build() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.from);
        message.setTo(this.to);
        message.setSubject(this.subject);
        message.setText(this.text);
        return message;
    }
}
