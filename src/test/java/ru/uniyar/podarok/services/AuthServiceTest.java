package ru.uniyar.podarok.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import ru.uniyar.podarok.dtos.JwtRequest;
import ru.uniyar.podarok.dtos.JwtResponse;
import ru.uniyar.podarok.dtos.RegistrationUserDto;
import ru.uniyar.podarok.dtos.UserDto;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.UserAlreadyExist;
import ru.uniyar.podarok.utils.JwtTokenUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void createNewUser_success_whenCorrectData() throws UserAlreadyExist {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(1, "test", "test@example.com", "12345");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("test");

        Mockito.when(userService.createNewUser(any(RegistrationUserDto.class))).thenReturn(user);

        UserDto result = authService.createNewUser(registrationUserDto);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        assertEquals("test", result.getFirstName());
    }

    @Test
    void createNewUser_exception_whenUserAlreadyExist() throws UserAlreadyExist {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(1, "test", "test@example.com", "12345");

        Mockito.when(userService.createNewUser(any(RegistrationUserDto.class)))
                .thenThrow(new UserAlreadyExist("Пользователь уже существует!"));

        assertThrows(UserAlreadyExist.class, () -> authService.createNewUser(registrationUserDto));
    }

    @Test
    void createAuthToken_success_whenCorrectData() {
        JwtRequest authRequest = new JwtRequest("test@example.com", "12345");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "test@example.com", "12345", new ArrayList<>());

        String generatedToken = "jwt-token";

        Mockito.when(userService.loadUserByUsername(authRequest.getEmail())).thenReturn(userDetails);
        Mockito.when(jwtTokenUtils.generateToken(userDetails)).thenReturn(generatedToken);

        JwtResponse result = authService.createAuthToken(authRequest);

        assertNotNull(result);
        assertEquals(generatedToken, result.getToken());
    }

    @Test
    void createAuthToken_exception_whenBadCredentials() {
        JwtRequest authRequest = new JwtRequest("test@example.com", "12345");

        Mockito.doThrow(new BadCredentialsException("Неверные данные!"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(BadCredentialsException.class, () -> authService.createAuthToken(authRequest));
    }
}