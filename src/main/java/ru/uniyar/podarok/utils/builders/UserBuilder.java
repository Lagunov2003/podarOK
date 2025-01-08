package ru.uniyar.podarok.utils.builders;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.entities.Cart;
import ru.uniyar.podarok.entities.Gift;
import ru.uniyar.podarok.entities.Notification;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.entities.Role;
import ru.uniyar.podarok.entities.SiteReviews;
import ru.uniyar.podarok.entities.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Строитель для создания объектов {@link User}.
 */
@Component
public class UserBuilder {
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private LocalDate registrationDate;
    private Boolean gender;
    private String phoneNumber;
    private List<Role> roles = new ArrayList<>();
    private List<Cart> cartItems = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private List<Gift> favorites = new ArrayList<>();
    private List<Notification> notifications = new ArrayList<>();
    private SiteReviews siteReviews;

    /**
     * Устанавливает пароль пользователя.
     *
     * @param password пароль пользователя
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * Устанавливает email пользователя.
     *
     * @param email email пользователя
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Устанавливает имя пользователя.
     *
     * @param firstName имя пользователя
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * Устанавливает фамилию пользователя.
     *
     * @param lastName фамилия пользователя
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Устанавливает дату рождения пользователя.
     *
     * @param dateOfBirth дата рождения пользователя
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    /**
     * Устанавливает дату регистрации пользователя.
     *
     * @param registrationDate дата регистрации пользователя
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    /**
     * Устанавливает пол пользователя.
     *
     * @param gender пол пользователя (true - мужской, false - женский)
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setGender(Boolean gender) {
        this.gender = gender;
        return this;
    }

    /**
     * Устанавливает номер телефона пользователя.
     *
     * @param phoneNumber номер телефона пользователя
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    /**
     * Устанавливает роли пользователя.
     *
     * @param roles список ролей пользователя
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    /**
     * Устанавливает товары в корзине пользователя.
     *
     * @param cartItems список товаров в корзине
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setCartItems(List<Cart> cartItems) {
        this.cartItems = cartItems;
        return this;
    }

    /**
     * Устанавливает заказы пользователя.
     *
     * @param orders список заказов пользователя
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setOrders(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    /**
     * Устанавливает избранные подарки пользователя.
     *
     * @param favorites список избранных подарков
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setFavorites(List<Gift> favorites) {
        this.favorites = favorites;
        return this;
    }

    /**
     * Устанавливает уведомления для пользователя.
     *
     * @param notifications список уведомлений пользователя
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    /**
     * Устанавливает отзывы пользователя о сайте.
     *
     * @param siteReviews объект с отзывами о сайте
     * @return текущий объект {@link UserBuilder} для дальнейшей настройки
     */
    public UserBuilder setSiteReviews(SiteReviews siteReviews) {
        this.siteReviews = siteReviews;
        return this;
    }

    /**
     * Строит объект {@link User} с заданными параметрами.
     *
     * @return новый объект {@link User}, содержащий все параметры, заданные через методы билдера
     */
    public User build() {
        User user = new User();
        user.setPassword(this.password);
        user.setEmail(this.email);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setDateOfBirth(this.dateOfBirth);
        user.setRegistrationDate(this.registrationDate);
        user.setGender(this.gender);
        user.setPhoneNumber(this.phoneNumber);
        user.setRoles(this.roles);
        user.setCartItems(this.cartItems);
        user.setOrders(this.orders);
        user.setFavorites(this.favorites);
        user.setNotifications(this.notifications);
        user.setSiteReviews(this.siteReviews);
        return user;
    }
}
