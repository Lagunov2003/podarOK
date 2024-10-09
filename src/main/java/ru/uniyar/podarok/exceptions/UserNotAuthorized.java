package ru.uniyar.podarok.exceptions;

public class UserNotAuthorized extends Exception {
    public UserNotAuthorized(String message) {
        super(message);
    }
}
