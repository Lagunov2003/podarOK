package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.uniyar.podarok.dtos.ChangeUserPasswordDto;
import ru.uniyar.podarok.dtos.CurrentUserDto;
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
        } catch(UserNotAuthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserDto updateUserDto) {
        try {
            if (updateUserDto.getFirstName().isEmpty() && updateUserDto.getLastName().isEmpty() &&
                    updateUserDto.getEmail().isEmpty() && updateUserDto.getPhoneNumber().isEmpty() &&
                    updateUserDto.getDateOfBirth() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Поля не должны быть пустыми!");
            }
            CurrentUserDto updatedUser = userService.updateUserProfile(updateUserDto);
            return ResponseEntity.ok(updatedUser);
        } catch(UserNotAuthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Все поля должны быть заполнены!");
        }
    }

    @PostMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> requestChangeUserPassword() {
        try {
            userService.requestChangeUserPassword();
            return ResponseEntity.ok().body("Перейдите по ссылке в письме для подтверждения смены пароля!");
        } catch(UserNotAuthorized e) {
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
        } catch(UserNotAuthorized e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException | NotValidCode e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch(FakeConfirmationCode e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch(ExpiredCode e) {
            return ResponseEntity.status(HttpStatus.GONE).body(e.getMessage());
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
