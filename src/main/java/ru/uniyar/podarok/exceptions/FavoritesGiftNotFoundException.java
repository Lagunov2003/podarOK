package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда подарка нет в списке избранных.
 * Это исключение используется при удалении подарка из списка избранных, когда указанного подарка в нём нет.
 */
public class FavoritesGiftNotFoundException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public FavoritesGiftNotFoundException(String message) {
        super(message);
    }
}
