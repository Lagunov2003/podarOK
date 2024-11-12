package ru.uniyar.podarok.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.uniyar.podarok.dtos.CartDto;
import ru.uniyar.podarok.entities.Cart;
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
    public ResponseEntity<?> showCart(@RequestParam(defaultValue = "1") int page){
        if (page <= 0) {
            return ResponseEntity.badRequest().body("Номер страницы должен быть больше 0.");
        }
        List<Cart> cartItems = cartService.getCart();
        if (cartItems.isEmpty()) {
            return ResponseEntity.ok("Корзина пуста!");
        }
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addItem(@RequestBody CartDto cartDto){
        try{
            if (cartDto.getItemCount() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Количество подарков должно быть не меньше 0!");
            }
            else {
                cartService.addGifts(cartDto.getGiftId(), cartDto.getItemCount());
                return ResponseEntity.ok(cartDto);
            }
        } catch (UserNotFoundException | UserNotAuthorizedException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateCartItem(@RequestBody CartDto cartDto){
        try{
            if (cartDto.getItemCount() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Количество подарков должно быть не меньше 0!");
            } else if (cartDto.getItemCount() == 0) {
                cartService.deleteGifts(cartDto.getGiftId());
                return ResponseEntity.ok("Подарок успешно удалён!");
            }
            else {
                cartService.changeGiftsAmount(cartDto.getGiftId(), cartDto.getItemCount());
                return ResponseEntity.ok(cartDto);
            }
        }
        catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/cart")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cleanCart(){
        cartService.cleanCart();
        return  ResponseEntity.ok("Корзина очищена!");
    }
}
