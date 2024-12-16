package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.CartDto;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.OrderItemDto;
import ru.uniyar.podarok.dtos.OrderRequestDto;
import ru.uniyar.podarok.entities.*;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.CartRepository;
import ru.uniyar.podarok.utils.Converters.GiftDtoConverter;

import java.math.BigDecimal;
import java.util.*;
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
    public void addGifts(Long giftId, Integer count) throws UserNotFoundException, UserNotAuthorizedException, GiftNotFoundException {
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
                .orElseThrow(() -> new NoSuchElementException("Подарок с id " + giftId + " не найден в корзине!"));
        cartItem.setItemCount(count);
        cartRepository.save(cartItem);
    }

    @Transactional
    public void cleanCart() throws UserNotFoundException, UserNotAuthorizedException {
        User user = userService.getCurrentAuthenticationUser();
        cartRepository.deleteAllByUserId(user.getId());
    }

    public List<CartDto> getCart() throws UserNotFoundException, UserNotAuthorizedException {
        User user = userService.getCurrentAuthenticationUser();
        List<Cart> cartItems = user.getCartItems();
        return cartItems.stream()
                .map(this::convertToCartDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void placeOrder(OrderRequestDto orderRequestDto) throws UserNotFoundException, UserNotAuthorizedException, GiftNotFoundException {
        User user = userService.getCurrentAuthenticationUser();
        Order order = createOrder(orderRequestDto, user);

        BigDecimal totalCost = BigDecimal.ZERO;
        Set<GiftOrder> giftOrders = new HashSet<>();

        for (OrderItemDto itemDto : orderRequestDto.getItems()) {
            GiftOrder giftOrder = createGiftOrder(itemDto, order, user);
            giftOrders.add(giftOrder);
            totalCost = totalCost.add(giftOrder.getGift().getPrice().multiply(BigDecimal.valueOf(giftOrder.getItemCount())));
        }

        order.setOrderCost(totalCost);
        order.setGiftOrders(giftOrders);

        orderService.placeOrder(order);
    }

    private GiftOrder createGiftOrder(OrderItemDto itemDto, Order order, User user) throws GiftNotFoundException {
        Gift gift = giftService.getGiftById(itemDto.getGiftId());
        GiftOrder giftOrder = new GiftOrder();
        giftOrder.setOrder(order);
        giftOrder.setGift(gift);
        giftOrder.setItemCount(itemDto.getItemCount());
        cartRepository.findItemByGiftIdAndUserId(itemDto.getGiftId(), user.getId())
                .ifPresent(cartRepository::delete);
        return giftOrder;
    }

    private Order createOrder(OrderRequestDto orderRequestDto, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus("Оформлен");
        order.setInformation(orderRequestDto.getInformation());
        order.setDeliveryDate(orderRequestDto.getDeliveryDate());
        order.setFromDeliveryTime(orderRequestDto.getFromDeliveryTime());
        order.setToDeliveryTime(orderRequestDto.getToDeliveryTime());
        order.setPayMethod(orderRequestDto.getPayMethod());
        order.setRecipientName(
                (orderRequestDto.getRecipientName() == null) ?
                        user.getLastName() + " " + user.getFirstName() :
                        orderRequestDto.getRecipientName()
        );
        order.setRecipientEmail(
                (orderRequestDto.getRecipientEmail() == null) ?
                        user.getEmail() :
                        orderRequestDto.getRecipientEmail()
        );
        order.setRecipientPhoneNumber(
                (orderRequestDto.getRecipientPhoneNumber() == null) ?
                        user.getPhoneNumber() :
                        orderRequestDto.getRecipientPhoneNumber()
        );
        return order;
    }
}
