package ru.uniyar.podarok.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.uniyar.podarok.entities.ConfirmationCode;
import ru.uniyar.podarok.entities.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConfirmationCodeRepositoryTest {
    @Autowired
    private ConfirmationCodeRepository confirmationCodeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    private ConfirmationCode code;
    @BeforeEach
    void setUp() {
        code = new ConfirmationCode();
        code.setId(1L);
        code.setExpiryDate(LocalDate.now().plusDays(1));
        code.setOwnUserId(1L);
        code.setCode("12345");
        User user = new User();
        user.setEmail("test");
        userRepository.save(user);
    }
    @BeforeEach
    void cleanDatabase() {
        entityManager.createNativeQuery("TRUNCATE TABLE confirmation_code RESTART IDENTITY CASCADE")
                .executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE").executeUpdate();
    }

    @Test
    void ConfirmationCodeRepository_SaveCode_ReturnSavedCode(){
        ConfirmationCode savedCode = confirmationCodeRepository.save(code);

        assertThat(savedCode).isNotNull();
        assertThat(savedCode.getId()).isGreaterThan(0);
        assertEquals(LocalDate.now().plusDays(1), savedCode.getExpiryDate());
        assertEquals(1, savedCode.getOwnUserId());
        assertEquals("12345", savedCode.getCode());
    }

    @Test
    void ConfirmationCodeRepository_FindByCode_ReturnFoundCode(){
        confirmationCodeRepository.save(code);
        ConfirmationCode code2 = new ConfirmationCode();
        code2.setId(2L);
        code2.setOwnUserId(1L);
        code2.setExpiryDate(LocalDate.now().plusDays(2));
        code2.setCode("23456");
        confirmationCodeRepository.save(code2);

        ConfirmationCode foundCode = confirmationCodeRepository.findByCode("12345").get();

        assertThat(foundCode).isNotNull();
        assertEquals("12345", foundCode.getCode());
    }

    @Test
    void ConfirmationCodeRepository_DeleteCode_ReturnCodeIsEmpty(){
        confirmationCodeRepository.save(code);

        confirmationCodeRepository.delete(code);

        Optional<ConfirmationCode> foundCode = confirmationCodeRepository.findByCode("12345");
        assertThat(foundCode).isEmpty();
    }
}
