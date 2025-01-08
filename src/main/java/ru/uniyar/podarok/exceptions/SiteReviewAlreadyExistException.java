package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда отзыв о сайте уже добавлен.
 * Это исключение используется при добавлении отзыва о сайте, когда отзыв от пользователя уже существует.
 */
public class SiteReviewAlreadyExistException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public SiteReviewAlreadyExistException(String message) {
        super(message);
    }
}
