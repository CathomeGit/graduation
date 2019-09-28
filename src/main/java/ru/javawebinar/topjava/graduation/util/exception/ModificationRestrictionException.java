package ru.javawebinar.topjava.graduation.util.exception;

import org.springframework.http.HttpStatus;

public class ModificationRestrictionException extends ApplicationException {
    public static final String MODIFICATION_RESTRICTION_EXCEPTION = "Modification is restricted";
    public ModificationRestrictionException(String message) {
        super(MODIFICATION_RESTRICTION_EXCEPTION, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, message);
    }
}