package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.uniyar.podarok.dtos.CartDto;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.OrderItemDto;
import ru.uniyar.podarok.dtos.OrderRequestDto;
import ru.uniyar.podarok.entities.Cart;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.GiftOrder;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.repositories.CartRepository;
import ru.uniyar.podarok.utils.builders.OrderBuilder;
import ru.uniyar.podarok.utils.converters.GiftDtoConverter;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для управления корзиной пользователя.
 */
@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private UserService userService;
    private GiftService giftService;
    private OrderService orderService;
    private GiftDtoConverter giftDtoConverter;

    /**
     * Преобразует объект {@link Cart} в объект {@link CartDto}.
     *
     * @param cart объект корзины
     * @return объект {@link CartDto}
     */
    private CartDto convertToCartDto(Cart cart) {
        GiftDto giftDto = giftDtoConverter.convertToGiftDto(cart.getGift());
        return new CartDto(cart.getItemCount(), giftDto);
    }

    /**
     * Добавляет товары в корзину.
     *
     * @param giftId идентификатор подарка
     * @param count  количество подарков
     * @throws UserNotFoundException если пользователь не найден
     * @throws UserNotAuthorizedException если пользователь не авторизован
     * @throws GiftNotFoundException если подарок не найден
     */
    @Transactional
    public void addGifts(Long giftId, Integer count)
            throws UserNotFoundException, UserNotAuthorizedException, GiftNotFoundException {
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

    /**
     * Удаляет товары из корзины.
     *
     * @param giftId идентификатор подарка
     */
    @Transactional
    public void deleteGifts(Long giftId) {
        cartRepository.findItemByGiftId(giftId).ifPresent(cartRepository::delete);
    }

    /**
     * Изменяет количество товаров в корзине.
     *
     * @param giftId идентификатор подарка
     * @param count новое количество подарков
     * @throws GiftNotFoundException если подарок отсутствует в корзине
     */
    @Transactional
    public void changeGiftsAmount(Long giftId, Integer count) throws GiftNotFoundException {
        Cart cartItem = cartRepository.findItemByGiftId(giftId)
                .orElseThrow(() -> new GiftNotFoundException("Подарок с id " + giftId + " не найден в корзине!"));
        cartItem.setItemCount(count);
        cartRepository.save(cartItem);
    }

    /**
     * Очищает корзину текущего авторизованного пользователя.
     *
     * @throws UserNotFoundException если пользователь не найден
     * @throws UserNotAuthorizedException если пользователь не авторизован
     */
    @Transactional
    public void cleanCart() throws UserNotFoundException, UserNotAuthorizedException {
        User user = userService.getCurrentAuthenticationUser();
        cartRepository.deleteAllByUserId(user.getId());
    }

    /**
     * Возвращает содержимое корзины текущего пользователя.
     *
     * @return список {@link CartDto}, представляющий содержимое корзины
     * @throws UserNotFoundException если пользователь не найден
     * @throws UserNotAuthorizedException если пользователь не авторизован
     */
    public List<CartDto> getCart() throws UserNotFoundException, UserNotAuthorizedException {
        User user = userService.getCurrentAuthenticationUser();
        List<Cart> cartItems = user.getCartItems();

        return cartItems.stream()
                .map(this::convertToCartDto)
                .collect(Collectors.toList());
    }

    /**
     * Оформляет заказ на основе содержимого корзины и данных заказа.
     *
     * @param orderRequestDto данные заказа
     * @throws UserNotFoundException если пользователь не найден
     * @throws UserNotAuthorizedException если пользователь не авторизован
     * @throws GiftNotFoundException если один из подарков не найден
     */
    @Transactional
    public void placeOrder(OrderRequestDto orderRequestDto)
            throws UserNotFoundException, UserNotAuthorizedException, GiftNotFoundException {
        User user = userService.getCurrentAuthenticationUser();
        Order order = createOrder(orderRequestDto, user);

        Set<GiftOrder> giftOrders = new HashSet<>();

        for (OrderItemDto itemDto : orderRequestDto.getItems()) {
            GiftOrder giftOrder = createGiftOrder(itemDto, order, user);
            giftOrders.add(giftOrder);
        }
        order.setGiftOrders(giftOrders);

        orderService.placeOrder(order);
    }

    /**
     * Создает объект {@link GiftOrder} для одного элемента заказа.
     *
     * @param itemDto данные элемента заказа
     * @param order заказ, к которому относится элемент
     * @param user текущий пользователь
     * @return объект {@link GiftOrder}
     * @throws GiftNotFoundException если подарок не найден
     */
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

    /**
     * Создает объект {@link Order} на основе данных запроса.
     *
     * @param orderRequestDto данные заказа
     * @param user текущий пользователь
     * @return объект {@link Order}
     */
    private Order createOrder(OrderRequestDto orderRequestDto, User user) {
        return new OrderBuilder()
                .user(user)
                .status("Оформлен")
                .information(orderRequestDto.getInformation())
                .deliveryDate(orderRequestDto.getDeliveryDate())
                .fromDeliveryTime(orderRequestDto.getFromDeliveryTime())
                .toDeliveryTime(orderRequestDto.getToDeliveryTime())
                .orderCost(orderRequestDto.getOrderCost())
                .payMethod(orderRequestDto.getPayMethod())
                .recipientName(
                        (orderRequestDto.getRecipientName() == null)
                                ? user.getLastName() + " " + user.getFirstName()
                                : orderRequestDto.getRecipientName()
                )
                .recipientEmail(
                        (orderRequestDto.getRecipientEmail() == null)
                                ? user.getEmail()
                                : orderRequestDto.getRecipientEmail()
                )
                .recipientPhoneNumber(
                        (orderRequestDto.getRecipientPhoneNumber() == null)
                                ? user.getPhoneNumber()
                                : orderRequestDto.getRecipientPhoneNumber()
                )
                .build();
    }
}
