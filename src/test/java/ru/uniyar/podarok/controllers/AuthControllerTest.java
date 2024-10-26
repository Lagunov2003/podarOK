package ru.uniyar.podarok.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import ru.uniyar.podarok.dtos.JwtRequest;
import ru.uniyar.podarok.dtos.JwtResponse;
import ru.uniyar.podarok.dtos.RegistrationUserDto;
import ru.uniyar.podarok.dtos.UserDto;
import ru.uniyar.podarok.exceptions.UserAlreadyExist;
import ru.uniyar.podarok.services.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @MockBean
    AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createNewUser_Success_whenCorrectData() throws Exception {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(1, "test", "test@example.com", "12345");
        UserDto userDto = new UserDto(1L, "test@example.com", "test");

        Mockito.when(authService.createNewUser(any(RegistrationUserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationUserDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(userDto)));
    }

    @Test
    void createNewUser_Conflict_whenUserAlreadyExists() throws Exception {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(1, "test", "test@example.com", "12345");

        Mockito.when(authService.createNewUser(any(RegistrationUserDto.class))).thenThrow(new UserAlreadyExist("Пользователь уже существует!"));

        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationUserDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Пользователь уже существует!"));
    }

    @Test
    void createAuthToken_Success_whenCorrectData() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("test@example.com", "12345");
        JwtResponse jwtResponse = new JwtResponse("");

        Mockito.when(authService.createAuthToken(any(JwtRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(jwtRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(jwtResponse)));
    }

    @Test
    void createAuthToken_Unauthorized_whenWrongData() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("test@example.com", "12345");

        Mockito.when(authService.createAuthToken(any(JwtRequest.class))).thenThrow(new BadCredentialsException("Неверные данные!"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(jwtRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Неверные данные!"));
    }
}
