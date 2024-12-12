package ru.uniyar.podarok.utils.Builder;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.entities.Notification;
import ru.uniyar.podarok.entities.User;

import java.time.LocalDateTime;

@Component
public class NotificationBuilder {
    private User user;
    private String itemValue;
    private LocalDateTime creationDateTime;

    public NotificationBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public NotificationBuilder setItemValue(String itemValue) {
        this.itemValue = itemValue;
        return this;
    }

    public NotificationBuilder setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
        return this;
    }

    public Notification build() {
        Notification notification = new Notification();
        notification.setUser(this.user);
        notification.setItemValue(this.itemValue);
        notification.setCreationDateTime(this.creationDateTime);
        return notification;
    }
}
