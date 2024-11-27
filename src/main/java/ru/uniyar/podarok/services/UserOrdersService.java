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
import ru.uniyar.podarok.utils.GiftDtoConverter;
import ru.uniyar.podarok.utils.OrderDtoConverter;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserOrdersService {
    private UserService userService;
    private GiftDtoConverter giftDtoConverter;
    private OrderDtoConverter orderDtoConverter;

    public List<Notification> getUsersNotifications() throws UserNotFoundException, UserNotAuthorizedException {
        return userService.getCurrentAuthenticationUser().getNotifications();
    }

    public List<GiftDto> getUsersFavorites() throws UserNotFoundException, UserNotAuthorizedException {
        List<Gift> favoriteGiftsList = userService.getCurrentAuthenticationUser().getFavorites();
        return giftDtoConverter.convertToGiftDtoList(favoriteGiftsList);
    }

    public List<OrderDto> getUsersOrdersHistory() throws UserNotFoundException, UserNotAuthorizedException {
        List<Order> ordersHistoryList = userService.getCurrentAuthenticationUser().getOrders();
        return ordersHistoryList.stream()
                .filter(order -> "Выполнен".equals(order.getStatus()))
                .map(orderDtoConverter::convertToOrderDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getUsersCurrentOrders() throws UserNotFoundException, UserNotAuthorizedException {
        List<Order> currentOrdersList = userService.getCurrentAuthenticationUser().getOrders();
        return currentOrdersList.stream()
                .filter(order -> List.of("Исполняется", "Доставляется").contains(order.getStatus()))
                .map(orderDtoConverter::convertToOrderDto)
                .collect(Collectors.toList());
    }
}
