package ru.uniyar.podarok.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.CartDto;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.OrderItemDto;
import ru.uniyar.podarok.dtos.OrderRequestDto;
import ru.uniyar.podarok.entities.Cart;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.CartRepository;
import ru.uniyar.podarok.utils.GiftDtoConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private UserService userService;
    private GiftService giftService;
    private OrderService orderService;
    private GiftDtoConverter giftDtoConverter;

    private CartDto convertToCartDto(Cart cart) {
        GiftDto giftDto = giftDtoConverter.convertToGiftDto(cart.getGift());
        return new CartDto(cart.getItemCount(), giftDto);
    }

    @Transactional
    public void addGifts(Long giftId, Integer count) throws UserNotFoundException, UserNotAuthorizedException, EntityNotFoundException {
        User user = userService.getCurrentAuthenticationUser();
        Gift gift = giftService.getGiftById(giftId);

        Optional<Cart> existingCartItem = cartRepository.findItemByGiftId(giftId);
        Cart cart;
        if (existingCartItem.isPresent()) {
            cart = existingCartItem.get();
            cart.setItemCount(cart.getItemCount() + count);
        } else {
            cart = new Cart();
            cart.setGift(gift);
            cart.setUser(user);
            cart.setItemCount(count);
        }
        cartRepository.save(cart);
    }

    @Transactional
    public void deleteGifts(Long giftId) {
        cartRepository.findItemByGiftId(giftId).ifPresent(cartRepository::delete);
    }

    @Transactional
    public void changeGiftsAmount(Long giftId, Integer count) throws NoSuchElementException {
        Cart cartItem = cartRepository.findItemByGiftId(giftId)
                .orElseThrow(() -> new NoSuchElementException("Подарок с ID " + giftId + " не найден в корзине."));
        cartItem.setItemCount(count);
        cartRepository.save(cartItem);
    }

    @Transactional
    public void cleanCart() {
        cartRepository.deleteAll();
    }

    public List<CartDto> getCart() throws UserNotFoundException, UserNotAuthorizedException {
        User user = userService.getCurrentAuthenticationUser();
        List<Cart> cartItems = cartRepository.findAll();
        return cartItems.stream()
                .map(this::convertToCartDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void placeOrder(OrderRequestDto orderRequestDto) throws UserNotFoundException, UserNotAuthorizedException, EntityNotFoundException {
        User user = userService.getCurrentAuthenticationUser();

        for (OrderItemDto itemDto : orderRequestDto.getItems()) {
            Gift gift = giftService.getGiftById(itemDto.getGiftId());

            Order order = new Order();
            order.setUser(user);
            order.setGift(gift);
            order.setStatus("Исполняется");
            order.setDeliveryDate(LocalDate.now().plusDays(3));
            order.setInformation("Информация о заказе получена. Дожидайтесь отправки подарка!");

            orderService.placeNewOrder(order);
            cartRepository.findItemByGiftIdAndUserId(itemDto.getGiftId(), user.getId())
                    .ifPresent(cartRepository::delete);
        }
    }
}
