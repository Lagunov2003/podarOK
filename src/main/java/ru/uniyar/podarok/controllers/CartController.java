package ru.uniyar.podarok.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.uniyar.podarok.dtos.CartItemDto;
import ru.uniyar.podarok.dtos.CartDto;
import ru.uniyar.podarok.dtos.OrderRequestDto;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.CartService;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Контроллер для управления корзиной пользователя и оформления заказов.
 * Все методы требуют авторизации пользователя.
 */
@Controller
@AllArgsConstructor
public class CartController {
    private CartService cartService;

    /**
     * Получить содержимое корзины.
     *
     * @param page номер страницы (по умолчанию 1).
     * @return содержимое корзины или сообщение о пустой корзине.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @GetMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> showCart(@RequestParam(defaultValue = "1") int page) throws UserNotAuthorizedException, UserNotFoundException {
        if (page <= 0) {
            return ResponseEntity.badRequest().body("Номер страницы должен быть больше 0!");
        }

        List<CartDto> cartItems = cartService.getCart();
        if (cartItems.isEmpty()) {
            return ResponseEntity.ok("Корзина пустая!");
        }
        return ResponseEntity.ok(cartItems);
    }

    /**
     * Добавить подарок в корзину.
     *
     * @param cartItemDto объект с данными о добавляемом подарке.
     * @return сообщение с подтверждением добавления.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws GiftNotFoundException если подарок с указанным id не найден.
     */
    @PostMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addItemToCart(@RequestBody CartItemDto cartItemDto)
            throws UserNotAuthorizedException, UserNotFoundException, GiftNotFoundException {
        if (cartItemDto.getItemCount() < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Количество подарков должно быть не меньше 1!");
        }
        cartService.addGifts(cartItemDto.getGiftId(), cartItemDto.getItemCount());
        return ResponseEntity.ok("Подарок успешно добавлен в корзину!");
    }

    /**
     * Изменить количество подарков в корзине или удалить подарок.
     *
     * @param cartItemDto объект с данными о подарке.
     * @return обновлённая корзина.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws NoSuchElementException если подарок не найден в корзине.
     */
    @PutMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateCartItem(@RequestBody CartItemDto cartItemDto)
            throws UserNotAuthorizedException, UserNotFoundException, NoSuchElementException {
        if (cartItemDto.getItemCount() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Количество подарков должно быть не меньше 0!");
        } else if (cartItemDto.getItemCount() == 0) {
            cartService.deleteGifts(cartItemDto.getGiftId());
        } else {
            cartService.changeGiftsAmount(cartItemDto.getGiftId(), cartItemDto.getItemCount());
        }
        return ResponseEntity.ok(cartService.getCart());
    }

    /**
     * Очистить корзину.
     *
     * @return сообщение с подтверждением очистки корзины.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь не найден.
     */
    @DeleteMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cleanCart() throws UserNotAuthorizedException, UserNotFoundException {
        cartService.cleanCart();
        return ResponseEntity.ok("Корзина очищена!");
    }

    /**
     * Оформить заказ.
     *
     * @param orderRequestDto объект с данными о заказе.
     * @return сообщение с подтверждением оформления заказа.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws GiftNotFoundException если подарок из корзины не найден.
     */
    @PostMapping("/order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addOrder(@RequestBody OrderRequestDto orderRequestDto)
            throws UserNotAuthorizedException, UserNotFoundException, GiftNotFoundException {
        if (orderRequestDto.getItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Количество подарков должно быть не меньше 1!");
        }  else {
            cartService.placeOrder(orderRequestDto);
        }
        return ResponseEntity.ok("Заказ успешно оформлен!");
    }
}
