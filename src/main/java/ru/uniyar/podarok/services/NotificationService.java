package ru.uniyar.podarok.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.uniyar.podarok.dtos.ChangeOrderStatusDto;
import ru.uniyar.podarok.entities.Notification;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.entities.User;
import ru.uniyar.podarok.utils.Builders.NotificationBuilder;

import java.time.LocalDateTime;

/**
 * Сервис для управления уведомлениями пользователей о заказах.
 */
@Service
@AllArgsConstructor
public class NotificationService {
    /**
     * Создает уведомление о новом оформленном заказе.
     *
     * @param order объект заказа, для которого будет создано уведомление
     */
    public void createPlaceOrderNotification(Order order) {
        User user = order.getUser();
        Notification notification = new NotificationBuilder()
                .setUser(user)
                .setItemValue("Заказ №" + order.getId() + " оформлен!")
                .setCreationDateTime(LocalDateTime.now())
                .build();
        user.getNotifications().add(notification);
    }

    /**
     * Создает уведомление об изменении статуса заказа.
     *
     * @param order объект заказа, для которого будет создано уведомление
     * @param changeOrderStatusDto данные с новым статусом заказа
     */
    public void createOrderStatusChangeNotification(Order order, ChangeOrderStatusDto changeOrderStatusDto) {
        User user = order.getUser();
        Notification notification = new NotificationBuilder()
                .setUser(user)
                .setItemValue(
                        "Статус Вашего заказа №"
                                + changeOrderStatusDto.getOrderId()
                                + " изменён на "
                                + changeOrderStatusDto.getNewOrderStatus()
                                + "!"
                )
                .setCreationDateTime(LocalDateTime.now())
                .build();
        user.getNotifications().add(notification);
    }
}
