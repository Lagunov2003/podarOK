package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.UserOrdersService;

/**
 * Контроллер для получения пользовательских уведомлений, избранных и заказов.
 * Все методы доступны только авторизованным пользователям.
 */
@RestController
@AllArgsConstructor
public class UserOrdersController {
    private UserOrdersService userOrdersService;

    /**
     * Получить список уведомлений пользователя.
     *
     * @return список уведомлений.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @GetMapping("/notifications")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getNotifications() throws UserNotAuthorizedException, UserNotFoundException {
        return ResponseEntity.ok(userOrdersService.getUsersNotifications());
    }

    /**
     * Получить список избранных товаров пользователя.
     *
     * @return список избранных товаров.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @GetMapping("/favorites")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getFavorites() throws UserNotAuthorizedException, UserNotFoundException {
        return ResponseEntity.ok(userOrdersService.getUsersFavorites());
    }

    /**
     * Получить историю заказов пользователя.
     *
     * @return список выполненных заказов.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @GetMapping("/ordersHistory")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOrdersHistory() throws UserNotAuthorizedException, UserNotFoundException {
        return ResponseEntity.ok(userOrdersService.getUsersOrdersHistory());
    }

    /**
     * Получить список текущих заказов пользователя.
     *
     * @return список текущих заказов.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @GetMapping("/currentOrders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCurrentOrders() throws UserNotAuthorizedException, UserNotFoundException {
        return ResponseEntity.ok(userOrdersService.getUsersCurrentOrders());
    }
}
