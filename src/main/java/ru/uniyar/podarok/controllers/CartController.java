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
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.services.CartService;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@AllArgsConstructor
public class CartController {
    private CartService cartService;

    @GetMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> showCart(@RequestParam(defaultValue = "1") int page) {
        if (page <= 0) {
            return ResponseEntity.badRequest().body("Номер страницы должен быть больше 0!");
        }
        try {
            List<CartDto> cartItems = cartService.getCart();
            if (cartItems.isEmpty()) {
                return ResponseEntity.ok("Корзина пуста!");
            }
            return ResponseEntity.ok(cartItems);
        } catch (UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addItemToCart(@RequestBody CartItemDto cartItemDto) {
        try {
            if (cartItemDto.getItemCount() < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Количество подарков должно быть не меньше 1!");
            }
            cartService.addGifts(cartItemDto.getGiftId(), cartItemDto.getItemCount());
            return ResponseEntity.ok("Подарок успешно добавлен в корзину!");
        } catch (UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (UserNotFoundException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateCartItem(@RequestBody CartItemDto cartItemDto) {
        try {
            if (cartItemDto.getItemCount() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Количество подарков должно быть не меньше 0!");
            } else if (cartItemDto.getItemCount() == 0) {
                cartService.deleteGifts(cartItemDto.getGiftId());
            } else {
                cartService.changeGiftsAmount(cartItemDto.getGiftId(), cartItemDto.getItemCount());
            }
            return ResponseEntity.ok(cartService.getCart());
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (UserNotFoundException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cleanCart() {
        cartService.cleanCart();
        return ResponseEntity.ok("Корзина очищена!");
    }

    @PostMapping("/order")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addOrder(@RequestBody OrderRequestDto orderRequestDto) {
        try {
            if (orderRequestDto.getItems().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Количество подарков должно быть не меньше 1!");
            }  else {
                cartService.placeOrder(orderRequestDto);
            }
            return ResponseEntity.ok("Заказ успешно оформлен!");
        } catch(UserNotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
