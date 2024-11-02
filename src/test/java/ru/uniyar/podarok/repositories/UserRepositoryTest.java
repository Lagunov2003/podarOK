package ru.uniyar.podarok.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.uniyar.podarok.entities.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    private User user;
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setPassword("12345");
        user.setGender(true);
        user.setFirstName("test");
        user.setLastName("user");
        user.setDateOfBirth(LocalDate.now());
        user.setPhoneNumber("80000000000");
        user.setRegistrationDate(LocalDate.now());
    }
    @BeforeEach
    void cleanDatabase() {
        entityManager.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE").executeUpdate();
    }

    @Test
    void UserRepository_SaveUser_ReturnsSavedUser(){
        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
        assertEquals("test@example.com", savedUser.getEmail());
        assertTrue(savedUser.isGender());
        assertEquals("test", savedUser.getFirstName());
        assertEquals("user", savedUser.getLastName());
        assertEquals(LocalDate.now(), savedUser.getDateOfBirth());
        assertEquals("80000000000", savedUser.getPhoneNumber());
        assertEquals(LocalDate.now(), savedUser.getRegistrationDate());
    }

    @Test
    void UserRepository_FindUserByEmail_ReturnsFoundUser(){
        userRepository.save(user);
        User user2 = new User();
        user2.setId(2);
        user2.setEmail("test2@example.com");
        userRepository.save(user2);

        User foundUser = userRepository.findUserByEmail("test@example.com").get();

        assertThat(foundUser).isNotNull();
        assertEquals("test@example.com", foundUser.getEmail());

    }

    @Test
    void UserRepository_DeleteUser_ReturnsUserIsEmpty(){
        userRepository.save(user);
        userRepository.delete(user);
        Optional<User> foundUser = userRepository.findUserByEmail("test@example.com");
        assertThat(foundUser).isEmpty();
    }

    @Test
    void UserRepository_UpdateUser_ReturnsUpdatedUser(){
        userRepository.save(user);
        user.setEmail("newTest@example.com");
        user.setPassword("54321");
        user.setGender(false);
        user.setFirstName("newTest");
        user.setLastName("newUser");
        user.setDateOfBirth(LocalDate.now().minusDays(1));
        user.setPhoneNumber("+70000000000");

        userRepository.save(user);

        User updatedUser = userRepository.findUserByEmail("newTest@example.com").get();
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getId()).isGreaterThan(0);
        assertEquals("newTest@example.com", updatedUser.getEmail());
        assertFalse(updatedUser.isGender());
        assertEquals("newTest", updatedUser.getFirstName());
        assertEquals("newUser", updatedUser.getLastName());
        assertEquals(LocalDate.now().minusDays(1), updatedUser.getDateOfBirth());
        assertEquals("+70000000000", updatedUser.getPhoneNumber());
    }


}
