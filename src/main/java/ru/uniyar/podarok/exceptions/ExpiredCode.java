package ru.uniyar.podarok.exceptions;

public class ExpiredCode extends Exception {
    public ExpiredCode(String message) {
        super(message);
    }
}
