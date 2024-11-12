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

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserOrdersService {
    private UserService userService;

    private List<GiftDto> convertToDto(List<Gift> gifts) {
        return gifts.stream().map(gift -> {
            String photoUrl = (gift.getPhotos() != null && !gift.getPhotos().isEmpty())
                    ? gift.getPhotos().get(0).getPhotoUrl()
                    : null;
            return new GiftDto(gift.getId(), gift.getName(), gift.getPrice(), photoUrl);
        }).collect(Collectors.toList());
    }

    private OrderDto convertToOrderDto(Order order) {
        Gift gift = order.getGift();

        GiftDto giftDto = new GiftDto(
                gift.getId(),
                gift.getName(),
                gift.getPrice(),
                gift.getPhotos().isEmpty() ? null : gift.getPhotos().get(0).getPhotoUrl()
        );

        return new OrderDto(
                order.getId(),
                order.getDeliveryDate(),
                order.getStatus(),
                order.getInformation(),
                giftDto
        );
    }

    public List<Notification> getUsersNotifications() throws UserNotFoundException, UserNotAuthorizedException {
        return userService.getCurrentAuthenticationUser().getNotifications();
    }

    public List<GiftDto> getUsersFavorites() throws UserNotFoundException, UserNotAuthorizedException {
        List<Gift> favoriteGiftsList = userService.getCurrentAuthenticationUser().getFavorites();
        return convertToDto(favoriteGiftsList);
    }

    public List<OrderDto> getUsersOrdersHistory() throws UserNotFoundException, UserNotAuthorizedException {
        List<Order> ordersHistoryList = userService.getCurrentAuthenticationUser().getOrders();
        return ordersHistoryList.stream()
                .filter(order -> "Выполнен".equals(order.getStatus()))
                .map(this::convertToOrderDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getUsersCurrentOrders() throws UserNotFoundException, UserNotAuthorizedException {
        List<Order> currentOrdersList = userService.getCurrentAuthenticationUser().getOrders();
        return currentOrdersList.stream()
                .filter(order -> List.of("Исполняется", "Доставляется").contains(order.getStatus()))
                .map(this::convertToOrderDto)
                .collect(Collectors.toList());
    }
}
