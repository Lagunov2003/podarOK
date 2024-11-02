package ru.uniyar.podarok.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilsTest {

    @Spy
    @InjectMocks
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private SecretKey secretKey;

    private String secret = "mockSecretKeyMockSecretKeyMockSecretKeyMockSecretKeyMockSecretKey";
    private Duration jwtLifeTime = Duration.ofMinutes(60);

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtTokenUtils, "secret", secret);
        ReflectionTestUtils.setField(jwtTokenUtils, "jwtLifeTime", jwtLifeTime);
        ReflectionTestUtils.invokeMethod(jwtTokenUtils, "init");
    }

    @Test
    public void JwtTokenUtils_GenerateToken_ReturnsNotNull() {
        UserDetails userDetails = new User("test", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        String token = jwtTokenUtils.generateToken(userDetails);

        assertNotNull(token, "Токен не должен быть null");
    }

    @Test
    public void JwtTokenUtils_GetUserEmail_ReturnsEmail() {
        UserDetails userDetails = new User("test", "password", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        String token = jwtTokenUtils.generateToken(userDetails);

        String email = jwtTokenUtils.getUserEmail(token);
        assertEquals("test", email, "Email пользователя должен совпадать с тем, что было передано");
    }

    @Test
    public void JwtTokenUtils_GetRoles_ReturnsListOfRoles() {
        UserDetails userDetails = new User("test", "password", List.of(new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN")));
        String token = jwtTokenUtils.generateToken(userDetails);

        List<String> roles = jwtTokenUtils.getRoles(token);
        assertEquals(2, roles.size(), "Должно быть две роли");
        assertEquals("ROLE_ADMIN", roles.get(0), "Первая роль должна быть ROLE_ADMIN");
        assertEquals("ROLE_USER", roles.get(1), "Вторая роль должна быть ROLE_USER");
    }
}
