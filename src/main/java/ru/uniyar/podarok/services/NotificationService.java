package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.OrderDataDto;
import ru.uniyar.podarok.entities.Notification;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.utils.Builder.NotificationBuilder;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationService {
    public void createPlaceOrderNotification(Order order) {
        User user = order.getUser();
        Notification notification = new NotificationBuilder()
                .setUser(user)
                .setItemValue("Заказ №" + order.getId() + " оформлен!")
                .setCreationDateTime(LocalDateTime.now())
                .build();
        user.getNotifications().add(notification);
    }

    public void createOrderStatusChangeNotification(Order order, OrderDataDto orderDataDto) {
        User user = order.getUser();
        Notification notification = new NotificationBuilder()
                .setUser(user)
                .setItemValue("Статус Вашего заказа №" + orderDataDto.getOrderId() + " изменён на " + orderDataDto.getNewOrderStatus() + "!")
                .setCreationDateTime(LocalDateTime.now())
                .build();
        user.getNotifications().add(notification);
    }
}
