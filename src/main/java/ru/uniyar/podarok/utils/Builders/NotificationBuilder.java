package ru.uniyar.podarok.utils.builders;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.entities.Notification;
import ru.uniyar.podarok.entities.User;

import java.time.LocalDateTime;

/**
 * Строитель для создания объектов {@link Notification}.
 */
@Component
public class NotificationBuilder {
    private User user;
    private String itemValue;
    private LocalDateTime creationDateTime;

    /**
     * Устанавливает пользователя, которому предназначено уведомление.
     *
     * @param user объект пользователя, которому будет отправлено уведомление
     * @return текущий объект {@link NotificationBuilder} для дальнейшей настройки
     */
    public NotificationBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    /**
     * Устанавливает значение элемента, о котором идет уведомление.
     *
     * @param itemValue строковое значение элемента для уведомления
     * @return текущий объект {@link NotificationBuilder} для дальнейшей настройки
     */
    public NotificationBuilder setItemValue(String itemValue) {
        this.itemValue = itemValue;
        return this;
    }

    /**
     * Устанавливает дату и время создания уведомления.
     *
     * @param creationDateTime дата и время, когда было создано уведомление
     * @return текущий объект {@link NotificationBuilder} для дальнейшей настройки
     */
    public NotificationBuilder setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
        return this;
    }

    /**
     * Строит объект {@link Notification} с заданными параметрами.
     *
     * @return новый объект {@link Notification}, содержащий все параметры, заданные через методы билдера
     */
    public Notification build() {
        Notification notification = new Notification();
        notification.setUser(this.user);
        notification.setItemValue(this.itemValue);
        notification.setCreationDateTime(this.creationDateTime);
        return notification;
    }
}
