package ru.uniyar.podarok.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender emailSender;

    public void sendRegistrationCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("podarOKService@yandex.ru");
        message.setTo(to);
        message.setSubject("Код для регистрации");
        message.setText("Ваш код для регистрации: " + code);
        emailSender.send(message);
    }
}
