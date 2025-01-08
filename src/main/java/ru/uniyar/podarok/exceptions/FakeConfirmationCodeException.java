package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда код подтверждения является поддельным.
 * Используется для обработки ситуации, когда код подтверждения был подделан.
 */
public class FakeConfirmationCodeException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public FakeConfirmationCodeException(String message) {
        super(message);
    }
}
