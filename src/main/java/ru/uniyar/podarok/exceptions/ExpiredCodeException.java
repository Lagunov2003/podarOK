package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда код подтверждения истёк.
 * Используется для обработки ситуации, когда код подтверждения был использован после истечения его срока действия.
 */
public class ExpiredCodeException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public ExpiredCodeException(String message) {
        super(message);
    }
}
