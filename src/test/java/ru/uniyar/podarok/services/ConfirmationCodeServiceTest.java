package ru.uniyar.podarok.services;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.uniyar.podarok.entities.ConfirmationCode;
import ru.uniyar.podarok.exceptions.ExpiredCode;
import ru.uniyar.podarok.exceptions.FakeConfirmationCode;
import ru.uniyar.podarok.exceptions.NotValidCode;
import ru.uniyar.podarok.repositories.ConfirmationCodeRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConfirmationCodeServiceTest {

    @Mock
    private ConfirmationCodeRepository confirmationCodeRepository;

    @Mock
    private EmailService emailService;

    @Spy
    @InjectMocks
    private ConfirmationCodeService confirmationCodeService;

    @Test
    void sendConfirmationCode_success_whenCorrectData() {
        long userId = 1L;
        String email = "test@example.com";
        String code = "12345";

        Mockito.doReturn(code).when(confirmationCodeService).generateConfirmationCode();

        confirmationCodeService.sendConfirmationCode(userId, email);

        ArgumentCaptor<ConfirmationCode> captor = ArgumentCaptor.forClass(ConfirmationCode.class);
        Mockito.verify(confirmationCodeRepository).save(captor.capture());
        ConfirmationCode savedCode = captor.getValue();
        assertEquals(userId, savedCode.getOwnUserId());
        assertEquals(code, savedCode.getCode());
        assertEquals(LocalDate.now().plusDays(1), savedCode.getExpiryDate());

        Mockito.verify(emailService).sendConfirmationLetter(email, code);
    }

    @Test
    void checkConfirmationCode_success_whenCorrectData() throws NotValidCode, ExpiredCode, FakeConfirmationCode {
        long userId = 1L;
        String code = "12345";
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setOwnUserId(userId);
        confirmationCode.setCode(code);
        confirmationCode.setExpiryDate(LocalDate.now().plusDays(1));

        Mockito.when(confirmationCodeRepository.findByCode(code)).thenReturn(Optional.of(confirmationCode));

        boolean result = confirmationCodeService.checkConfirmationCode(userId, code);

        assertTrue(result);
        Mockito.verify(confirmationCodeRepository).delete(confirmationCode);
    }

    @Test
    void checkConfirmationCode_throwNotValidCode_whenNoCodeItRepository() {
        long userId = 1L;
        String invalidCode = "99999";

        Mockito.when(confirmationCodeRepository.findByCode(invalidCode)).thenReturn(Optional.empty());

        assertThrows(NotValidCode.class, () -> {
            confirmationCodeService.checkConfirmationCode(userId, invalidCode);
        });
    }

    @Test
    void checkConfirmationCode_throwFakeConfirmationCode_whenUsingOtherUserCode() {
        long userId = 1L;
        long otherUserId = 2L;
        String code = "12345";
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setOwnUserId(otherUserId);
        confirmationCode.setCode(code);
        confirmationCode.setExpiryDate(LocalDate.now().plusDays(1));

        Mockito.when(confirmationCodeRepository.findByCode(code)).thenReturn(Optional.of(confirmationCode));

        assertThrows(FakeConfirmationCode.class, () -> {
            confirmationCodeService.checkConfirmationCode(userId, code);
        });
    }

    @Test
    void checkConfirmationCode_throwExpiredCode_whenCodeIsExpired() {
        long userId = 1L;
        String code = "12345";
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setOwnUserId(userId);
        confirmationCode.setCode(code);
        confirmationCode.setExpiryDate(LocalDate.now().minusDays(1));

        Mockito.when(confirmationCodeRepository.findByCode(code)).thenReturn(Optional.of(confirmationCode));

        assertThrows(ExpiredCode.class, () -> {
            confirmationCodeService.checkConfirmationCode(userId, code);
        });

        Mockito.verify(confirmationCodeRepository).delete(confirmationCode);
    }
}

