package ru.javawebinar.topjava.graduation.util.exception;

import org.springframework.http.HttpStatus;

public class ModificationRestrictionException extends ApplicationException {
    public ModificationRestrictionException(String message) {
        super(message, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }
}