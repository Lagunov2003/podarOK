package ru.uniyar.podarok.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.uniyar.podarok.exceptions.ExpiredCodeException;
import ru.uniyar.podarok.exceptions.FakeConfirmationCodeException;
import ru.uniyar.podarok.exceptions.GiftNotFoundException;
import ru.uniyar.podarok.exceptions.NotValidCodeException;
import ru.uniyar.podarok.exceptions.OrderNotFoundException;
import ru.uniyar.podarok.exceptions.SiteReviewNotFoundException;
import ru.uniyar.podarok.exceptions.UserAlreadyExistException;
import ru.uniyar.podarok.exceptions.UserNotAuthorizedException;
import ru.uniyar.podarok.exceptions.UserNotFoundException;

import java.util.NoSuchElementException;


/**
 * Глобальный обработчик исключений.
 * Класс перехватывает исключения, возникающие в приложении, и возвращает
 * HTTP-ответы с соответствующими кодами статуса.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Обработка исключений при доступе без авторизации.
     *
     * @param e исключение типа {@link UserNotAuthorizedException}.
     * @return HTTP-ответ с кодом 401 (UNAUTHORIZED) и сообщением об ошибке.
     */
    @ExceptionHandler(UserNotAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleUserNotAuthorizedException(UserNotAuthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * Обработка исключений, если пользователь не найден.
     *
     * @param e исключение типа {@link UserNotFoundException}.
     * @return HTTP-ответ с кодом 404 (NOT FOUND) и сообщением об ошибке.
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Обработка исключений, если пользователь с указанным email уже существует.
     *
     * @param e исключение типа {@link UserAlreadyExistException}.
     * @return HTTP-ответ с кодом 409 (CONFLICT) и сообщением об ошибке.
     */
    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleUserAlreadyExistException(UserAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    /**
     * Обработка исключений, если введены неверные данные пользователя.
     *
     * @param e исключение типа {@link org.springframework.security.authentication.BadCredentialsException}.
     * @return HTTP-ответ с кодом 401 (UNAUTHORIZED) и сообщением об ошибке.
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handlerBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * Обработка исключений, если код подтверждения недействителен.
     *
     * @param e исключение типа {@link FakeConfirmationCodeException}.
     * @return HTTP-ответ с кодом 403 (FORBIDDEN) и сообщением об ошибке.
     */
    @ExceptionHandler(FakeConfirmationCodeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleFakeConfirmationCodeException(FakeConfirmationCodeException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    /**
     * Обработка исключений, если код подтверждения истёк.
     *
     * @param e исключение типа {@link ExpiredCodeException}.
     * @return HTTP-ответ с кодом 410 (GONE) и сообщением об ошибке.
     */
    @ExceptionHandler(ExpiredCodeException.class)
    @ResponseStatus(HttpStatus.GONE)
    public ResponseEntity<String> handleExpiredCodeException(ExpiredCodeException e) {
        return ResponseEntity.status(HttpStatus.GONE).body(e.getMessage());
    }

    /**
     * Обработка исключений, если код подтверждения невалиден.
     *
     * @param e исключение типа {@link NotValidCodeException}.
     * @return HTTP-ответ с кодом 404 (NOT FOUND) и сообщением об ошибке.
     */
    @ExceptionHandler(NotValidCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotValidCodeException(NotValidCodeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Обработка ошибок валидации данных.
     *
     * @param e исключение типа {@link MethodArgumentNotValidException}.
     * @return HTTP-ответ с кодом 400 (BAD REQUEST) и сообщением об ошибке.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Некоторые поля заполнены некорректно!");
    }

    /**
     * Обработка исключений JWT токенов.
     *
     * @param e исключение типа {@link ExpiredJwtException},
     * {@link SignatureException} или {@link MalformedJwtException}.
     * @return HTTP-ответ с кодом 401 (UNAUTHORIZED) и соответствующим сообщением об ошибке.
     */
    @ExceptionHandler({ExpiredJwtException.class, SignatureException.class, MalformedJwtException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleJwtExceptions(Exception e) {
        if (e instanceof ExpiredJwtException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Токен устарел!");
        } else if (e instanceof SignatureException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Некорректный токен!");
        } else if (e instanceof MalformedJwtException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неправильный формат токена!");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ошибка авторизации!");
    }

    /**
     * Обработка исключений отсутствия заказа с указанным id.
     *
     * @param e исключение типа {@link OrderNotFoundException}.
     * @return HTTP-ответ с кодом 404 (NOT FOUND) и соответствующим сообщением об ошибке.
     */
    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Обработка исключений отсутствия подарка с указанным id.
     *
     * @param e исключение типа {@link GiftNotFoundException}.
     * @return HTTP-ответ с кодом 404 (NOT FOUND) и соответствующим сообщением об ошибке.
     */
    @ExceptionHandler(GiftNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleGiftNotFoundException(GiftNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Обработка исключений отсутствия отзыва о сайте с указанным id.
     *
     * @param e исключение типа {@link SiteReviewNotFoundException}.
     * @return HTTP-ответ с кодом 404 (NOT FOUND) и соответствующим сообщением об ошибке.
     */
    @ExceptionHandler(SiteReviewNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleSiteReviewNotFoundException(SiteReviewNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Обработка исключений отсутствия подарка в корзине.
     *
     * @param e исключение типа {@link java.util.NoSuchElementException}.
     * @return HTTP-ответ с кодом 404 (NOT FOUND) и соответствующим сообщением об ошибке.
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Обработка общих исключений.
     *
     * @param e исключение типа {@link Exception}.
     * @return HTTP-ответ с кодом 500 (INTERNAL SERVER ERROR) и сообщением об ошибке.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGlobalException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                "Внутренняя ошибка сервера: " + e.getMessage()
        );
    }
}
