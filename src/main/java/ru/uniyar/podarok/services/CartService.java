package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.entities.Cart;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.CartRepository;
import ru.uniyar.podarok.repositories.projections.GiftProjection;

import java.util.ArrayList;
import java.lang.IllegalArgumentException;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private UserService userService;
    private GiftService giftService;
    public void addGifts(Long giftId, Integer count) throws UserNotFoundException, UserNotAuthorizedException {
        Cart cart = new Cart();
        Gift gift = giftService.getGiftById(giftId);
        cart.setGift(gift);
        User user = userService.getCurrentAuthenticationUser();
        cart.setUser(user);
        cart.setItemCount(count);
        cartRepository.save(cart);
    }
    public void deleteGifts(Long giftId){
        Cart cartItem = cartRepository.findItemByGiftId(giftId).get();
        cartRepository.deleteById(cartItem.getId());

    }
    public void changeGiftsAmount(Long giftId, Integer count){
        Cart cartItem = cartRepository.findItemByGiftId(giftId).get();
        cartItem.setItemCount(count);
        cartRepository.save(cartItem);
    }

    public void cleanCart() {
        cartRepository.deleteAll();
    }


    public List<Cart> getCart(){
        return cartRepository.findAll();
    }
}
