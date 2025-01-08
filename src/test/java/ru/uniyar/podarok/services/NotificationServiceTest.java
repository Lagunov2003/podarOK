package ru.uniyar.podarok.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.uniyar.podarok.dtos.ChangeOrderStatusDto;
import ru.uniyar.podarok.entities.Notification;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.entities.User;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void NotificationService_CreatePlaceOrderNotification_VerifiesNotificationCreated() {
        Order order = new Order();
        order.setId(1L);
        User user = new User();
        user.setNotifications(new ArrayList<>());
        order.setUser(user);

        notificationService.createPlaceOrderNotification(order);

        List<Notification> notifications = user.getNotifications();
        assertEquals(1, notifications.size());
        Notification notification = notifications.get(0);
        assertEquals("Заказ №1 оформлен!", notification.getItemValue());
        assertEquals(user, notification.getUser());
        assertNotNull(notification.getCreationDateTime());
    }

    @Test
    public void NotificationService_CreateOrderStatusChangeNotification_VerifiesNotificationCreated() {
        Order order = new Order();
        order.setId(1L);
        User user = new User();
        user.setNotifications(new ArrayList<>());
        order.setUser(user);
        ChangeOrderStatusDto changeOrderStatusDto = new ChangeOrderStatusDto();
        changeOrderStatusDto.setOrderId(1L);
        changeOrderStatusDto.setNewOrderStatus("Оформлен");

        notificationService.createOrderStatusChangeNotification(order, changeOrderStatusDto);

        List<Notification> notifications = user.getNotifications();
        assertEquals(1, notifications.size());
        Notification notification = notifications.get(0);
        assertEquals("Статус Вашего заказа №1 изменён на Оформлен!", notification.getItemValue());
        assertEquals(user, notification.getUser());
        assertNotNull(notification.getCreationDateTime());
    }
}
