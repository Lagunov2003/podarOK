package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.uniyar.podarok.dtos.ChangeUserPasswordDto;
import ru.uniyar.podarok.dtos.UpdateUserDto;
import ru.uniyar.podarok.dtos.UserDto;
import ru.uniyar.podarok.exceptions.EmptyUserData;
import ru.uniyar.podarok.exceptions.UserNotAuthorized;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.UserService;

@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> showProfile() {
        try {
            UserDto userDto = userService.getCurrentUserProfile();
            return ResponseEntity.ok(userDto);
        } catch(UserNotAuthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserDto updateUserDto) {
        try {
            UserDto updatedUser = userService.updateUserProfile(updateUserDto);
            return ResponseEntity.ok(updatedUser);
        } catch(UserNotAuthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(EmptyUserData e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@RequestBody ChangeUserPasswordDto changeUserPasswordDto) {
        try {
            userService.changeUserPassword(changeUserPasswordDto);
            return ResponseEntity.ok().body("Перейдите по ссылке в письме для подтверждения смены пароля!");
        } catch(UserNotAuthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка генерации ссылки для подтверждения пароля!");
        }
    }

    @GetMapping("/confirmChanges")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> confirmChangePassword(@RequestParam("token") String token){
        try {
            userService.confirmChangeUserPassword(token);
            return ResponseEntity.ok().body("Пароль успешно изменён!");
        } catch(UserNotAuthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Некорректная ссылка для подтверждения пароля!");
        }
    }

    @DeleteMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteUser() {
        try {
            userService.deleteCurrentUser();
            return ResponseEntity.ok("Пользователь успешно удалён!");
        } catch(UserNotAuthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
