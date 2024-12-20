package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.GiftDto;
import ru.uniyar.podarok.dtos.OrderDto;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.Notification;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;
import ru.uniyar.podarok.utils.converters.GiftDtoConverter;
import ru.uniyar.podarok.utils.converters.OrderDtoConverter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с заказами, избранными подарками и уведомлениями пользователя.
 */
@Service
@AllArgsConstructor
public class UserOrdersService {
    private UserService userService;
    private GiftDtoConverter giftDtoConverter;
    private OrderDtoConverter orderDtoConverter;

    /**
     * Получает уведомления текущего аутентифицированного пользователя.
     *
     * @return список уведомлений {@link Notification} пользователя.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     */
    public List<Notification> getUsersNotifications() throws UserNotFoundException, UserNotAuthorizedException {
        return userService.getCurrentAuthenticationUser().getNotifications();
    }

    /**
     * Получает список избранных подарков текущего аутентифицированного пользователя.
     *
     * @return список объектов {@link GiftDto} с данными избранных подарков.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     */
    public List<GiftDto> getUsersFavorites() throws UserNotFoundException, UserNotAuthorizedException {
        List<Gift> favoriteGiftsList = userService.getCurrentAuthenticationUser().getFavorites();
        return giftDtoConverter.convertToGiftDtoList(favoriteGiftsList);
    }

    /**
     * Получает историю заказов текущего аутентифицированного пользователя, которые были доставлены или отменены.
     *
     * @return список объектов {@link OrderDto} с данными заказов.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     */
    public List<OrderDto> getUsersOrdersHistory() throws UserNotFoundException, UserNotAuthorizedException {
        List<Order> ordersHistoryList = userService.getCurrentAuthenticationUser().getOrders();
        return ordersHistoryList.stream()
                .filter(order -> List.of("Доставлен", "Отменён").contains(order.getStatus()))
                .map(orderDtoConverter::convertToOrderDto)
                .collect(Collectors.toList());
    }

    /**
     * Получает текущие заказы текущего аутентифицированного пользователя,
     * которые находятся в статусах "Оформлен", "Собран" или "В пути".
     *
     * @return список объектов {@link OrderDto} с данными текущих заказов.
     * @throws UserNotFoundException если пользователь не найден.
     * @throws UserNotAuthorizedException если пользователь не авторизован.
     */
    public List<OrderDto> getUsersCurrentOrders() throws UserNotFoundException, UserNotAuthorizedException {
        List<Order> currentOrdersList = userService.getCurrentAuthenticationUser().getOrders();
        return currentOrdersList.stream()
                .filter(order -> List.of("Оформлен", "Собран", "В пути").contains(order.getStatus()))
                .map(orderDtoConverter::convertToOrderDto)
                .collect(Collectors.toList());
    }
}
