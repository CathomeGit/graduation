package ru.javawebinar.topjava.graduation.util.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApplicationException {
    public static final String NOT_FOUND_EXCEPTION = "Not found entity with {0}";

    public NotFoundException(String arg) {
        super(NOT_FOUND_EXCEPTION, HttpStatus.UNPROCESSABLE_ENTITY, arg);
    }
}