package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.uniyar.podarok.entities.UserEntity;
import ru.uniyar.podarok.exceptions.UserAlreadyExist;
import ru.uniyar.podarok.services.UserService;

@RestController
@AllArgsConstructor
public class AuthController {
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<Object> registration(@RequestBody UserEntity user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok().body("Перейдите по ссылке, отправленной на Ваш email!");
        } catch (UserAlreadyExist e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping("/activation")
//    public ResponseEntity<Object> activation(@RequestParam String activationCode) {
////        userService.activateUser(activationCode);
//        return ResponseEntity.ok().body("Регистрация завершена!");
//    }
}
