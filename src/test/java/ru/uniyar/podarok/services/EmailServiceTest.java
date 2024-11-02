package ru.uniyar.podarok.services;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailMessageCaptor;

    @Test
    void EmailService_SendWelcomeLetter_ReturnsSentMessage() {
        String email = "test@example.com";
        String firstName = "test";

        emailService.sendWelcomeLetter(email, firstName);

        Mockito.verify(emailSender, Mockito.times(1)).send(mailMessageCaptor.capture());
        SimpleMailMessage sentMessage = mailMessageCaptor.getValue();
        assertEquals("podarOKService@yandex.ru", sentMessage.getFrom());
        assertEquals(email, sentMessage.getTo()[0]);
        assertEquals("Благодарим за регистрацию в сервисе podarOK!", sentMessage.getSubject());
        assertEquals(firstName + ", благодарим Вас за регистрацию в Нашем сервисе по подбору и заказу подарков podarOk!", sentMessage.getText());
    }

    @Test
    void EmailService_SendConfirmationLetter_ReturnsSentMessage() {
        String email = "test@example.com";
        String code = "12345";

        emailService.sendConfirmationLetter(email, code);

        Mockito.verify(emailSender, Mockito.times(1)).send(mailMessageCaptor.capture());
        SimpleMailMessage sentMessage = mailMessageCaptor.getValue();
        assertEquals("podarOKService@yandex.ru", sentMessage.getFrom());
        assertEquals(email, sentMessage.getTo()[0]);
        assertEquals("Смена пароля в сервисе podarOK!", sentMessage.getSubject());
        assertEquals("Для изменения пароля перейдите по ссылке: localhost:8080/confirmChanges?code=" + code, sentMessage.getText());
    }

    @Test
    void EmailService_SendUpdateEmailNotifications_ReturnsSentMessage() {
        String oldEmail = "old@example.com";
        String newEmail = "new@example.com";

        emailService.sendUpdateEmailNotifications(oldEmail, newEmail);

        Mockito.verify(emailSender, Mockito.times(1)).send(mailMessageCaptor.capture());
        SimpleMailMessage sentMessage = mailMessageCaptor.getValue();
        assertEquals("podarOKService@yandex.ru", sentMessage.getFrom());
        assertEquals(oldEmail, sentMessage.getTo()[0]);
        assertEquals(newEmail, sentMessage.getTo()[1]);
        assertEquals("Смена электронной почты в сервисе podarOK!", sentMessage.getSubject());
        assertEquals(String.format("Электронная почта в сервисе podarOK была изменена с %s на %s!", oldEmail, newEmail), sentMessage.getText());
    }
}
