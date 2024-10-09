package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {
    private JavaMailSender emailSender;
    private ActivationCodeService activationCodeService;

    public void sendActivationCode(String addressee) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("podarOKService@yandex.ru");
        message.setTo(addressee);
        message.setSubject("Код для регистрации");
        message.setText("Ваш код для регистрации: " + activationCodeService.generationActivationCode(addressee));
        emailSender.send(message);
    }


}
