package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда отзыв о сайте не найден.
 * Используется для обработки ситуации, когда запрос на получение отзыва не может быть выполнен,
 * потому что указанный отзыв не существует.
 */
public class SiteReviewNotFoundException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public SiteReviewNotFoundException(String message) {
        super(message);
    }
}
