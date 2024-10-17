package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender emailSender;

    public void sendWelcomeLetter(String email, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("podarOKService@yandex.ru");
        message.setTo(email);
        message.setSubject("Благодарим за регистрацию в сервисе podarOK!");
        message.setText(firstName + ", благодарим Вас за регистрацию в Нашем сервисе по подбору и заказу подарков podarOk!");
        emailSender.send(message);
    }
}
