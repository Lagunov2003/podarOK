package ru.uniyar.podarok.exceptions;

/**
 * Исключение, которое выбрасывается, когда пользователь не найден в системе.
 * Используется для обработки ситуации, когда операции с пользователем не могут быть выполнены,
 * потому что указанный пользователь не существует в базе данных.
 */
public class UserNotFoundException extends Exception {
    /**
     * Конструктор для создания исключения с сообщением.
     *
     * @param message сообщение об ошибке, объясняющее, почему возникло исключение.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
