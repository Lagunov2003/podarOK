package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.uniyar.podarok.dtos.JwtRequest;
import ru.uniyar.podarok.dtos.RegistrationUserDto;
import ru.uniyar.podarok.exceptions.UserAlreadyExistException;
import ru.uniyar.podarok.services.AuthService;

/**
 * Контроллер для обработки операций аутентификации и регистрации пользователей.
 */
@RestController
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    /**
     * Получить страницу авторизации.
     *
     * @return сообщение о доступности страницы авторизации.
     */
    @GetMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok("Открылась страница авторизации!");
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param registrationUserDto DTO с данными для регистрации пользователя.
     * @return данные пользователя при успешной регистрации.
     * @throws UserAlreadyExistException если пользователь с таким email уже существует.
     */
    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(
            @RequestBody RegistrationUserDto registrationUserDto
    ) throws UserAlreadyExistException {
        return ResponseEntity.ok(authService.createNewUser(registrationUserDto));
    }

    /**
     * Создание токена аутентификации.
     *
     * @param authRequest DTO с данными для аутентификации пользователя.
     * @return JWT-токен при успешной аутентификации.
     * @throws BadCredentialsException если предоставленные данные неверны.
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) throws BadCredentialsException {
        return ResponseEntity.ok(authService.createAuthToken(authRequest));
    }
}
