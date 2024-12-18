package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда введённый код является недействительным.
 * Используется для обработки ситуаций, когда код подтверждения не является правильным.
 */
public class NotValidCodeException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public NotValidCodeException(String message) {
        super(message);
    }
}
