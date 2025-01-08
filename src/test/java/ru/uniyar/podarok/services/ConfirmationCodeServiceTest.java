package ru.uniyar.podarok.services;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import ru.uniyar.podarok.entities.ConfirmationCode;
import ru.uniyar.podarok.exceptions.ExpiredCodeException;
import ru.uniyar.podarok.exceptions.FakeConfirmationCodeException;
import ru.uniyar.podarok.exceptions.NotValidCodeException;
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

    long userId = 1L;
    String email = "test@example.com";
    String code = "12345";
    @Test
    void ConfirmationCodeService_SendConfirmationCode_ReturnsSentCode() {
        Mockito.doReturn(code).when(confirmationCodeService).generateConfirmationCode();

        confirmationCodeService.sendConfirmationCode(userId, email);

        ArgumentCaptor<ConfirmationCode> captor = ArgumentCaptor.forClass(ConfirmationCode.class);
        Mockito.verify(confirmationCodeRepository).save(captor.capture());
        ConfirmationCode sentCode = captor.getValue();
        assertEquals(userId, sentCode.getOwnUserId());
        assertEquals(code, sentCode.getCode());
        assertEquals(LocalDate.now().plusDays(1), sentCode.getExpiryDate());
        Mockito.verify(emailService).sendConfirmationLetter(email, code);
    }

    @Test
    void ConfirmationCodeService_CheckConfirmationCode_ReturnsIsCodeCorrect()
            throws NotValidCodeException, ExpiredCodeException, FakeConfirmationCodeException {
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
    void ConfirmationCodeService_CheckConfirmationCode_ThrowsNotValidCodeException() {
        String invalidCode = "99999";
        Mockito.when(confirmationCodeRepository.findByCode(invalidCode)).thenReturn(Optional.empty());

        assertThrows(NotValidCodeException.class,
                () -> confirmationCodeService.checkConfirmationCode(userId, invalidCode));
    }

    @Test
    void ConfirmationCodeService_CheckConfirmationCode_ThrowsFakeConfirmationCodeException() {
        long otherUserId = 2L;
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setOwnUserId(otherUserId);
        confirmationCode.setCode(code);
        confirmationCode.setExpiryDate(LocalDate.now().plusDays(1));
        Mockito.when(confirmationCodeRepository.findByCode(code)).thenReturn(Optional.of(confirmationCode));

        assertThrows(FakeConfirmationCodeException.class, () -> {
            confirmationCodeService.checkConfirmationCode(userId, code);
        });
    }

    @Test
    void ConfirmationCodeService_CheckConfirmationCode_ThrowsExpiredCodeException() {
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setOwnUserId(userId);
        confirmationCode.setCode(code);
        confirmationCode.setExpiryDate(LocalDate.now().minusDays(1));
        Mockito.when(confirmationCodeRepository.findByCode(code)).thenReturn(Optional.of(confirmationCode));

        assertThrows(ExpiredCodeException.class,
                () -> confirmationCodeService.checkConfirmationCode(userId, code));
        Mockito.verify(confirmationCodeRepository).delete(confirmationCode);
    }
}

