package ru.uniyar.podarok.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.uniyar.podarok.dtos.ChangeUserPasswordDto;
import ru.uniyar.podarok.dtos.CurrentUserDto;
import ru.uniyar.podarok.dtos.ForgotUserPasswordDto;
import ru.uniyar.podarok.dtos.UpdateUserDto;
import ru.uniyar.podarok.exceptions.*;
import ru.uniyar.podarok.services.UserService;

/**
 * Контроллер обработки действий с профилем пользователя.
 */
@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;

    /**
     * Получить профиль текущего пользователя.
     * Доступно только для авторизованных пользователей.
     *
     * @return профиль текущего пользователя.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     */
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CurrentUserDto> showProfile() throws UserNotFoundException, UserNotAuthorizedException {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    /**
     * Обновить профиль текущего пользователя.
     * Доступно только для авторизованных пользователей.
     *
     * @param updateUserDto DTO с данными для обновления.
     * @return обновленный профиль пользователя.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws org.springframework.web.bind.MethodArgumentNotValidException если введены некорректные данные.
     */
    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CurrentUserDto> updateProfile(@Valid @RequestBody UpdateUserDto updateUserDto)
            throws UserNotFoundException, UserNotAuthorizedException {
        return ResponseEntity.ok(userService.updateUserProfile(updateUserDto));
    }

    /**
     * Удалить текущего пользователя.
     * Доступно только для авторизованных пользователей.
     *
     * @return сообщение об успешном удалении пользователя.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @DeleteMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deleteUser() throws UserNotFoundException, UserNotAuthorizedException {
        userService.deleteCurrentUser();
        return ResponseEntity.ok("Пользователь успешно удалён!");
    }

    /**
     * Запрос на изменение пароля авторизованного пользователя.
     * Доступно только для авторизованных пользователей.
     *
     * @return сообщение об успешной отправке ссылки для подтверждения смены пароля.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     */
    @PostMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> requestChangeUserPassword() throws UserNotFoundException, UserNotAuthorizedException {
        userService.requestChangeUserPassword();
        return ResponseEntity.ok("Перейдите по ссылке в письме для подтверждения смены пароля!");
    }

    /**
     * Подтвердить изменение пароля авторизованного пользователя.
     * Доступно только для авторизованных пользователей.
     *
     * @param code код подтверждения.
     * @param changeUserPasswordDto DTO с новым паролем.
     * @return сообщение об успешном изменении пароля.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws ExpiredCodeException если код подтверждения истек.
     * @throws FakeConfirmationCodeException если код подтверждения подделанный.
     * @throws NotValidCodeException если код подтверждения некорректен.
     */
    @PostMapping("/confirmChanges")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> confirmChangeUserPassword(
            @RequestParam("code") String code,
            @RequestBody ChangeUserPasswordDto changeUserPasswordDto
    ) throws UserNotFoundException, UserNotAuthorizedException, ExpiredCodeException,
            FakeConfirmationCodeException, NotValidCodeException {

        if (!changeUserPasswordDto.getPassword().equals(changeUserPasswordDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Пароли не совпадают!");
        }

        if (changeUserPasswordDto.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body("Пароль должен быть минимум 6 символов!");
        }

        userService.confirmChangeUserPassword(code, changeUserPasswordDto);
        return ResponseEntity.ok("Пароль успешно изменён!");
    }

    /**
     * Обработать запрос на восстановление пароля.
     *
     * @param forgotUserPasswordDto DTO с email пользователя.
     * @return сообщение об успешной отправке ссылки для восстановления пароля.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     */
    @PostMapping("/forgot")
    public ResponseEntity<String> processForgotPassword(@RequestBody ForgotUserPasswordDto forgotUserPasswordDto)
            throws UserNotFoundException {
        userService.sendPasswordResetLink(forgotUserPasswordDto.getEmail());
        return ResponseEntity.ok("Перейдите по ссылке в письме для восстановления пароля!");
    }

    /**
     * Сбросить пароль пользователя.
     *
     * @param token токен для сброса пароля.
     * @param changeUserPasswordDto DTO с новым паролем.
     * @return сообщение об успешном изменении пароля.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(
            @RequestParam("token") String token,
            @RequestBody ChangeUserPasswordDto changeUserPasswordDto
    ) throws UserNotFoundException {

        if (!changeUserPasswordDto.getPassword().equals(changeUserPasswordDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Пароли не совпадают!");
        }

        if (changeUserPasswordDto.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body("Пароль должен быть минимум 6 символов!");
        }

        userService.confirmChangePassword(token, changeUserPasswordDto);
        return ResponseEntity.ok("Пароль успешно изменён!");
    }
}
