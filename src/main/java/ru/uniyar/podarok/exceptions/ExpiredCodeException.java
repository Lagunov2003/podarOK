package ru.uniyar.podarok.exceptions;

public class ExpiredCodeException extends Exception {
    public ExpiredCodeException(String message) {
        super(message);
    }
}
