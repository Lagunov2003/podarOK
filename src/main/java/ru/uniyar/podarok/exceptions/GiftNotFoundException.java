package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда подарок не найден.
 * Это исключение используется при поиске подарков в базе данных, когда указанный подарок не существует.
 */
public class GiftNotFoundException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public GiftNotFoundException(String message) {
        super(message);
    }
}
