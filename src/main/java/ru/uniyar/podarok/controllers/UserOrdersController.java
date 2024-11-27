package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.UserOrdersService;

@RestController
@AllArgsConstructor
public class UserOrdersController {
    private UserOrdersService userOrdersService;

    @GetMapping("/notifications")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getNotifications() {
        try {
            return ResponseEntity.ok(userOrdersService.getUsersNotifications());
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/favorites")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getFavorites() {
        try {
            return ResponseEntity.ok(userOrdersService.getUsersFavorites());
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/ordersHistory")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOrdersHistory() {
        try {
            return ResponseEntity.ok(userOrdersService.getUsersOrdersHistory());
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/currentOrders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCurrentOrders() {
        try {
            return ResponseEntity.ok(userOrdersService.getUsersCurrentOrders());
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
