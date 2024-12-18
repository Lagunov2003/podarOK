package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда пользователь не авторизован для выполнения определённых действий..
 * Это исключение используется, когда пользователь пытается выполнить действие, требующее прав, которых у него нет.
 */
public class UserNotAuthorizedException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
