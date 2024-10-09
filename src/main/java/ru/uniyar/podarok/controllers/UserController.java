package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.uniyar.podarok.entities.UserEntity;
import ru.uniyar.podarok.exceptions.UserAlreadyExist;
import ru.uniyar.podarok.services.EmailService;
import ru.uniyar.podarok.services.UserService;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private EmailService emailService;

    // TODO
    // GET запрос на получение кода (связать с проверкой на актуальность кода)

//    @PostMapping("/activation")
//
//    @PostMapping("/registration")
//    public ResponseEntity<String> registration(@RequestBody UserEntity user) {
//        try {
//            userService.registration(user);
//            return ResponseEntity.ok("Всё гуд");
//        } catch (UserAlreadyExist e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
}
