package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.ConfirmationCode;
import ru.uniyar.podarok.exceptions.ExpiredCodeException;
import ru.uniyar.podarok.exceptions.FakeConfirmationCodeException;
import ru.uniyar.podarok.exceptions.NotValidCodeException;
import ru.uniyar.podarok.repositories.ConfirmationCodeRepository;

import java.time.LocalDate;
import java.util.Objects;
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

    public void sendConfirmationCode(Long userId, String email) {
        String code = generateConfirmationCode();
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setOwnUserId(userId);
        confirmationCode.setCode(code);
        confirmationCode.setExpiryDate(LocalDate.now().plusDays(1));
        confirmationCodeRepository.save(confirmationCode);
        emailService.sendConfirmationLetter(email, code);
    }

    public boolean checkConfirmationCode(Long userId, String code) throws NotValidCodeException, ExpiredCodeException, FakeConfirmationCodeException {
        ConfirmationCode confirmationCode = confirmationCodeRepository.findByCode(code)
                .orElseThrow(() -> new NotValidCodeException("Некорректный код подтверждения!"));
        if (!Objects.equals(confirmationCode.getOwnUserId(), userId)) {
            throw new FakeConfirmationCodeException("Подделанный код пользователя!");
        }
        if (!isExpiryDateValid(confirmationCode.getExpiryDate())) {
            confirmationCodeRepository.delete(confirmationCode);
            throw new ExpiredCodeException("Срок валидности кода истёк!");
        }
        confirmationCodeRepository.delete(confirmationCode);
        return true;
    }
}
