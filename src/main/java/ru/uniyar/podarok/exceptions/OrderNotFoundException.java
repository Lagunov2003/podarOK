package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда заказ не найден.
 * Это исключение используется, когда заказ с заданным идентификатором не существует в системе.
 */
public class OrderNotFoundException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}
