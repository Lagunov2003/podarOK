package ru.uniyar.podarok.utils.Builder;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.entities.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public UserBuilder setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public UserBuilder setGender(Boolean gender) {
        this.gender = gender;
        return this;
    }

    public UserBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public UserBuilder setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    public UserBuilder setCartItems(List<Cart> cartItems) {
        this.cartItems = cartItems;
        return this;
    }

    public UserBuilder setOrders(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    public UserBuilder setFavorites(List<Gift> favorites) {
        this.favorites = favorites;
        return this;
    }

    public UserBuilder setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    public UserBuilder setSiteReviews(SiteReviews siteReviews) {
        this.siteReviews = siteReviews;
        return this;
    }

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