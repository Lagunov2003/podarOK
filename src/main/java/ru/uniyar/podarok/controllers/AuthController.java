package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.UserAlreadyExist;
import ru.uniyar.podarok.services.UserService;

@RestController
@AllArgsConstructor
public class AuthController {
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<Object> registration(@RequestBody User user) {
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
