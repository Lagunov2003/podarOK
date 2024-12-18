package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.ConfirmationCode;
import ru.uniyar.podarok.exceptions.ExpiredCodeException;
import ru.uniyar.podarok.exceptions.FakeConfirmationCodeException;
import ru.uniyar.podarok.exceptions.NotValidCodeException;
import ru.uniyar.podarok.repositories.ConfirmationCodeRepository;
import ru.uniyar.podarok.utils.Builders.ConfirmationCodeBuilder;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;

/**
 * Сервис для управления кодами подтверждения.
 */
@Service
@AllArgsConstructor
public class ConfirmationCodeService {
    private ConfirmationCodeRepository confirmationCodeRepository;
    private EmailService emailService;

    /**
     * Генерирует случайный шестизначный код подтверждения.
     *
     * @return строка, содержащая сгенерированный код.
     */
    public String generateConfirmationCode() {
        final int minBound = 100000;
        final int maxBound = 900000;
        return String.valueOf(minBound + new Random().nextInt(maxBound));
    }

    /**
     * Проверяет, является ли дата истечения срока действия кода подтверждения актуальной.
     *
     * @param expiryDate дата истечения срока действия.
     * @return {@code true}, если код ещё действителен, иначе {@code false}.
     */
    public boolean isExpiryDateValid(LocalDate expiryDate) {
        return LocalDate.now().isBefore(expiryDate);
    }

    /**
     * Отправляет код подтверждения пользователю по электронной почте.
     *
     * @param userId идентификатор пользователя.
     * @param email  адрес электронной почты пользователя.
     */
    public void sendConfirmationCode(Long userId, String email) {
        String code = generateConfirmationCode();
        ConfirmationCode confirmationCode = new ConfirmationCodeBuilder()
                .setOwnUserId(userId)
                .setCode(code)
                .setExpiryDate(LocalDate.now().plusDays(1))
                .build();

        confirmationCodeRepository.save(confirmationCode);
        emailService.sendConfirmationLetter(email, code);
    }

    /**
     * Проверяет корректность кода подтверждения.
     *
     * @param userId идентификатор пользователя.
     * @param code код подтверждения, введённый пользователем.
     * @return {@code true}, если код действителен.
     * @throws NotValidCodeException если код не найден.
     * @throws ExpiredCodeException если срок действия кода истёк.
     * @throws FakeConfirmationCodeException если код не соответствует пользователю.
     */
    public boolean checkConfirmationCode(Long userId, String code)
            throws NotValidCodeException, ExpiredCodeException, FakeConfirmationCodeException {
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
