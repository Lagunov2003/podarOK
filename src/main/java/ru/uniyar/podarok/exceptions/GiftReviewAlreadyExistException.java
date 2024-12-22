package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда отзыв о подарке уже добавлен.
 * Это исключение используется при добавлении отзыва о подарке, когда отзыв от пользователя уже существует.
 */
public class GiftReviewAlreadyExistException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public GiftReviewAlreadyExistException(String message) {
        super(message);
    }
}
