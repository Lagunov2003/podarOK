package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.utils.builders.EmailMessageBuilder;

/**
 * Сервис для отправки электронных писем пользователям.
 */
@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender emailSender;
    private static final String MAILING_EMAIL = "podarOKService@yandex.ru";

    /**
     * Отправляет приветственное письмо после регистрации пользователя.
     *
     * @param email электронная почта пользователя.
     * @param firstName имя пользователя.
     */
    public void sendWelcomeLetter(String email, String firstName) {
        SimpleMailMessage message = new EmailMessageBuilder()
                .from(MAILING_EMAIL)
                .to(email)
                .subject("Благодарим за регистрацию в сервисе podarOK!")
                .text(firstName
                        + ", благодарим Вас за регистрацию в Нашем сервисе по подбору и заказу подарков podarOk!")
                .build();
        emailSender.send(message);
    }

    /**
     * Отправляет письмо с ссылкой для подтверждения смены пароля.
     *
     * @param email электронная почта пользователя.
     * @param code код для подтверждения смены пароля.
     */
    public void sendConfirmationLetter(String email, String code) {
        SimpleMailMessage message = new EmailMessageBuilder()
                .from(MAILING_EMAIL)
                .to(email)
                .subject("Смена пароля в сервисе podarOK!")
                .text("Для изменения пароля перейдите по ссылке: localhost:8080/confirmChanges?code=" + code)
                .build();
        emailSender.send(message);
    }


    /**
     * Отправляет уведомление о смене электронной почты.
     *
     * @param email текущий адрес электронной почты пользователя.
     * @param newEmail новый адрес электронной почты пользователя.
     */
    public void sendUpdateEmailNotifications(String email, String newEmail) {
        SimpleMailMessage message = new EmailMessageBuilder()
                .from(MAILING_EMAIL)
                .to(email, newEmail)
                .subject("Смена электронной почты в сервисе podarOK!")
                .text(String.format("Электронная почта в сервисе podarOK была изменена с %s на %s!", email, newEmail))
                .build();
        emailSender.send(message);
    }

    /**
     * Отправляет письмо с ссылкой для восстановления пароля.
     *
     * @param email электронная почта пользователя.
     * @param token токен для восстановления пароля.
     */
    public void sendPasswordResetLetter(String email, String token) {
        SimpleMailMessage message = new EmailMessageBuilder()
                .from(MAILING_EMAIL)
                .to(email)
                .subject("Восстановление пароля в сервисе podarOK!")
                .text("Для восстановления пароля перейдите по ссылке: localhost:8080/resetPassword?token=" + token)
                .build();
        emailSender.send(message);
    }
}
