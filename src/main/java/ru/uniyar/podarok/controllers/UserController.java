package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.uniyar.podarok.dtos.UserDto;
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
}
