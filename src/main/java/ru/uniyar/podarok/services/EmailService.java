package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.utils.Builders.MailMessageBuilder;

@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender emailSender;
    private static final String mailingEmail = "podarOKService@yandex.ru";

    public void sendWelcomeLetter(String email, String firstName) {
        SimpleMailMessage message = new MailMessageBuilder()
                .from(mailingEmail)
                .to(email)
                .subject("Благодарим за регистрацию в сервисе podarOK!")
                .text(firstName + ", благодарим Вас за регистрацию в Нашем сервисе по подбору и заказу подарков podarOk!")
                .build();
        emailSender.send(message);
    }

    public void sendConfirmationLetter(String email, String code) {
        SimpleMailMessage message = new MailMessageBuilder()
                .from(mailingEmail)
                .to(email)
                .subject("Смена пароля в сервисе podarOK!")
                .text("Для изменения пароля перейдите по ссылке: localhost:8080/confirmChanges?code=" + code)
                .build();
        emailSender.send(message);
    }

    public void sendUpdateEmailNotifications(String email, String newEmail) {
        SimpleMailMessage message = new MailMessageBuilder()
                .from(mailingEmail)
                .to(email, newEmail)
                .subject("Смена электронной почты в сервисе podarOK!")
                .text(String.format("Электронная почта в сервисе podarOK была изменена с %s на %s!", email, newEmail))
                .build();
        emailSender.send(message);
    }

    public void sendPasswordResetLetter(String email, String token) {
        SimpleMailMessage message = new MailMessageBuilder()
                .from(mailingEmail)
                .to(email)
                .subject("Восстановление пароля в сервисе podarOK!")
                .text("Для восстановления пароля перейдите по ссылке: localhost:8080/resetPassword?token=" + token)
                .build();
        emailSender.send(message);
    }
}
