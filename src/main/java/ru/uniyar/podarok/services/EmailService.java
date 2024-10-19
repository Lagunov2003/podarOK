package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender emailSender;
    private static final String mailingEmail = "podarOKService@yandex.ru";

    public void sendWelcomeLetter(String email, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailingEmail);
        message.setTo(email);
        message.setSubject("Благодарим за регистрацию в сервисе podarOK!");
        message.setText(firstName + ", благодарим Вас за регистрацию в Нашем сервисе по подбору и заказу подарков podarOk!");
        emailSender.send(message);
    }

    public void sendConfirmationLetter(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailingEmail);
        message.setTo(email);
        message.setSubject("Смена пароля в сервисе podarOK!");
        message.setText("Для изменения пароля перейдите по ссылке: localhost:8080/confirmChanges?code=" + code);
        emailSender.send(message);
    }
}
