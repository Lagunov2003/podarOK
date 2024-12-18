package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда пользователь уже существует в системе.
 * Используется при попытке зарегистрировать пользователя с уже существующим адресом электронной почты.
 */
public class UserAlreadyExistException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
