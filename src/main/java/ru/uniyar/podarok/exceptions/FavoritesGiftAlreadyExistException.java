package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда подарок есть в списке избранных.
 * Это исключение используется при добавлении подарка в список избранных, когда указанный подарок уже есть там.
 */
public class FavoritesGiftAlreadyExistException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public FavoritesGiftAlreadyExistException(String message) {
        super(message);
    }
}
