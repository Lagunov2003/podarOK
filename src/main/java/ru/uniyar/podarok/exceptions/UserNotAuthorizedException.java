package ru.uniyar.podarok.exceptions;

public class UserNotAuthorizedException extends Exception {
    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
