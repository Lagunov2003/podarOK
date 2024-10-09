package ru.uniyar.podarok.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.uniyar.podarok.service.EmailService;

import java.util.Random;

@Controller
@RequestMapping("/api/register")
@AllArgsConstructor
public class EmailController {
    private EmailService emailService;

    @PostMapping
    public String register(@RequestParam String email) {
        Random random = new Random();
        String generatedCode = String.valueOf(100000 + random.nextInt(900000));
        emailService.sendRegistrationCode(email, generatedCode);

        return "Код для регистрации отправлен на " + email;
    }
}
