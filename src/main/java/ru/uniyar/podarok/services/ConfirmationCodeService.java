package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.ConfirmationCode;
import ru.uniyar.podarok.exceptions.ExpiredCode;
import ru.uniyar.podarok.exceptions.FakeConfirmationCode;
import ru.uniyar.podarok.exceptions.NotValidCode;
import ru.uniyar.podarok.repositories.ConfirmationCodeRepository;

import java.time.LocalDate;
import java.util.Random;

@Service
@AllArgsConstructor
public class ConfirmationCodeService {
    private ConfirmationCodeRepository confirmationCodeRepository;
    private EmailService emailService;

    public String generateConfirmationCode() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    public boolean isExpiryDateValid(LocalDate expiryDate) {
        return LocalDate.now().isBefore(expiryDate);
    }

    public void sendConfirmationCode(long userId, String email) {
        String code = generateConfirmationCode();
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setOwnUserId(userId);
        confirmationCode.setCode(code);
        confirmationCode.setExpiryDate(LocalDate.now().plusDays(1));
        confirmationCodeRepository.save(confirmationCode);
        emailService.sendConfirmationLetter(email, code);
    }

    public boolean checkConfirmationCode(long userId, String code) throws NotValidCode, ExpiredCode, FakeConfirmationCode {
        ConfirmationCode confirmationCode = confirmationCodeRepository.findByCode(code)
                .orElseThrow(() -> new NotValidCode("Некорректный код подтверждения!"));
        if (confirmationCode.getOwnUserId() != userId) {
            throw new FakeConfirmationCode("Подделаный код пользователя!");
        }
        if (!isExpiryDateValid(confirmationCode.getExpiryDate())) {
            confirmationCodeRepository.delete(confirmationCode);
            throw new ExpiredCode("Срок валидности кода истёк!");
        }
        confirmationCodeRepository.delete(confirmationCode);
        return true;
    }
}
