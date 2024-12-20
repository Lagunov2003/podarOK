package ru.uniyar.podarok.utils.builders;

import org.springframework.stereotype.Component;
import ru.uniyar.podarok.entities.Order;
import ru.uniyar.podarok.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Строитель для создания объектов {@link Order}.
 */
@Component
public class OrderBuilder {
    private User user;
    private String status;
    private String information;
    private LocalDate deliveryDate;
    private LocalTime fromDeliveryTime;
    private BigDecimal orderCost;
    private LocalTime toDeliveryTime;
    private String payMethod;
    private String recipientName;
    private String recipientEmail;
    private String recipientPhoneNumber;

    /**
     * Устанавливает пользователя, создавшего заказ.
     *
     * @param user объект {@link User}
     * @return текущий объект {@link OrderBuilder} для дальнейшей настройки
     */
    public OrderBuilder user(User user) {
        this.user = user;
        return this;
    }

    /**
     * Устанавливает статус заказа.
     *
     * @param status статус заказа
     * @return текущий объект {@link OrderBuilder} для дальнейшей настройки
     */
    public OrderBuilder status(String status) {
        this.status = status;
        return this;
    }

    /**
     * Устанавливает дополнительную информацию о заказе.
     *
     * @param information информация о заказе
     * @return текущий объект {@link OrderBuilder} для дальнейшей настройки
     */
    public OrderBuilder information(String information) {
        this.information = information;
        return this;
    }

    /**
     * Устанавливает дату доставки заказа.
     *
     * @param deliveryDate дата доставки
     * @return текущий объект {@link OrderBuilder} для дальнейшей настройки
     */
    public OrderBuilder deliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    /**
     * Устанавливает время начала доставки.
     *
     * @param fromDeliveryTime начало временного окна доставки
     * @return текущий объект {@link OrderBuilder} для дальнейшей настройки
     */
    public OrderBuilder fromDeliveryTime(LocalTime fromDeliveryTime) {
        this.fromDeliveryTime = fromDeliveryTime;
        return this;
    }

    /**
     * Устанавливает время завершения доставки.
     *
     * @param toDeliveryTime завершение временного окна доставки
     * @return текущий объект {@link OrderBuilder} для дальнейшей настройки
     */
    public OrderBuilder toDeliveryTime(LocalTime toDeliveryTime) {
        this.toDeliveryTime = toDeliveryTime;
        return this;
    }

    /**
     * Устанавливает метод оплаты.
     *
     * @param payMethod метод оплаты
     * @return текущий объект {@link OrderBuilder} для дальнейшей настройки
     */
    public OrderBuilder payMethod(String payMethod) {
        this.payMethod = payMethod;
        return this;
    }

    /**
     * Устанавливает имя получателя.
     *
     * @param recipientName имя получателя
     * @return текущий объект {@link OrderBuilder} для дальнейшей настройки
     */
    public OrderBuilder recipientName(String recipientName) {
        this.recipientName = recipientName;
        return this;
    }

    /**
     * Устанавливает email получателя.
     *
     * @param recipientEmail email получателя
     * @return текущий объект {@link OrderBuilder} для дальнейшей настройки
     */
    public OrderBuilder recipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
        return this;
    }

    /**
     * Устанавливает номер телефона получателя.
     *
     * @param recipientPhoneNumber номер телефона получателя
     * @return текущий объект {@link OrderBuilder} для дальнейшей настройки
     */
    public OrderBuilder recipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
        return this;
    }

    /**
     * Устанавливает номер телефона получателя.
     *
     * @param orderCost стоимость заказа
     * @return текущий объект {@link OrderBuilder} для дальнейшей настройки
     */
    public OrderBuilder orderCost(BigDecimal orderCost) {
        this.orderCost = orderCost;
        return this;
    }

    /**
     * Строит объект {@link Order} на основе заданных параметров.
     *
     * @return объект {@link Order}, содержащий все параметры заказа
     */
    public Order build() {
        Order order = new Order();
        order.setUser(this.user);
        order.setStatus(this.status);
        order.setInformation(this.information);
        order.setDeliveryDate(this.deliveryDate);
        order.setFromDeliveryTime(this.fromDeliveryTime);
        order.setToDeliveryTime(this.toDeliveryTime);
        order.setOrderCost(this.orderCost);
        order.setPayMethod(this.payMethod);
        order.setRecipientName(this.recipientName);
        order.setRecipientEmail(this.recipientEmail);
        order.setRecipientPhoneNumber(this.recipientPhoneNumber);
        return order;
    }
}
