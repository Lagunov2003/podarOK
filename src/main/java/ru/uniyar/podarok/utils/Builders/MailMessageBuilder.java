package ru.uniyar.podarok.utils.builders;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;


/**
 * Строитель для создания объектов {@link SimpleMailMessage}.
 */
@Component("mailMessageBuilderMain")
public class MailMessageBuilder {
    private String from;
    private String[] to;
    private String subject;
    private String text;

    /**
     * Устанавливает отправителя сообщения.
     *
     * @param from адрес электронной почты отправителя
     * @return текущий объект {@link MailMessageBuilder} для дальнейшей настройки
     */
    public MailMessageBuilder from(String from) {
        this.from = from;
        return this;
    }

    /**
     * Устанавливает получателей сообщения.
     *
     * @param to массив адресов электронной почты получателей
     * @return текущий объект {@link MailMessageBuilder} для дальнейшей настройки
     */
    public MailMessageBuilder to(String... to) {
        this.to = to;
        return this;
    }

    /**
     * Устанавливает тему сообщения.
     *
     * @param subject тема письма
     * @return текущий объект {@link MailMessageBuilder} для дальнейшей настройки
     */
    public MailMessageBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Устанавливает текст сообщения.
     *
     * @param text тело письма
     * @return текущий объект {@link MailMessageBuilder} для дальнейшей настройки
     */
    public MailMessageBuilder text(String text) {
        this.text = text;
        return this;
    }

    /**
     * Строит объект {@link SimpleMailMessage} на основе заданных параметров.
     *
     * @return объект {@link SimpleMailMessage}, который содержит все параметры сообщения
     */
    public SimpleMailMessage build() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.from);
        message.setTo(this.to);
        message.setSubject(this.subject);
        message.setText(this.text);
        return message;
    }
}
