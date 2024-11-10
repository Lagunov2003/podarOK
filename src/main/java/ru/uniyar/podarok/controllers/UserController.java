package ru.uniyar.podarok.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.uniyar.podarok.dtos.ChangeUserPasswordDto;
import ru.uniyar.podarok.dtos.CurrentUserDto;
import ru.uniyar.podarok.dtos.ForgotUserPasswordDto;
import ru.uniyar.podarok.dtos.UpdateUserDto;
import ru.uniyar.podarok.exceptions.*;
import ru.uniyar.podarok.services.UserService;

@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> showProfile() {
        try {
            CurrentUserDto userDto = userService.getCurrentUserProfile();
            return ResponseEntity.ok(userDto);
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateUserDto updateUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Некоторые поля заполнены некорректно!");
        }
        try {
            CurrentUserDto updatedUser = userService.updateUserProfile(updateUserDto);
            return ResponseEntity.ok(updatedUser);
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Все поля должны быть заполнены!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> requestChangeUserPassword() {
        try {
            userService.requestChangeUserPassword();
            return ResponseEntity.ok().body("Перейдите по ссылке в письме для подтверждения смены пароля!");
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/confirmChanges")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> confirmChangeUserPassword(@RequestParam("code") String code, @RequestBody ChangeUserPasswordDto changeUserPasswordDto){
        try {
            if (!changeUserPasswordDto.getPassword().equals(changeUserPasswordDto.getConfirmPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пароли не совпадают!");
            }
            if (changeUserPasswordDto.getPassword().length() < 6) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пароль должен быть минимум 6 символов!");
            }
            userService.confirmChangeUserPassword(code, changeUserPasswordDto);
            return ResponseEntity.ok().body("Пароль успешно изменён!");
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException | NotValidCodeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(FakeConfirmationCodeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch(ExpiredCodeException e) {
            return ResponseEntity.status(HttpStatus.GONE).body(e.getMessage());
        }
    }

    @DeleteMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteUser() {
        try {
            userService.deleteCurrentUser();
            return ResponseEntity.ok("Пользователь успешно удалён!");
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> processForgotPassword(@RequestBody ForgotUserPasswordDto forgotUserPasswordDto) {
        try {
            userService.sendPasswordResetLink(forgotUserPasswordDto.getEmail());
            return ResponseEntity.ok("Перейдите по ссылке в письме для восстановления пароля!");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestBody ChangeUserPasswordDto changeUserPasswordDto) {
        try {
            if (!changeUserPasswordDto.getPassword().equals(changeUserPasswordDto.getConfirmPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пароли не совпадают!");
            }
            if (changeUserPasswordDto.getPassword().length() < 6) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Пароль должен быть минимум 6 символов!");
            }
            userService.confirmChangePassword(token, changeUserPasswordDto);
            return ResponseEntity.ok().body("Пароль успешно изменён!");
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Токен устарел!");
        } catch (SignatureException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Некорректный токен!");
        }  catch (MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильный формат токена!");
        }
    }
}
