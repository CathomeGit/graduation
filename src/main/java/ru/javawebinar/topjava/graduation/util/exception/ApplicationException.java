package ru.javawebinar.topjava.graduation.util.exception;

import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class ApplicationException extends RuntimeException {

    private final String msgCode;
    private final HttpStatus httpStatus;
    private final String[] args;

    public ApplicationException(String msgCode, HttpStatus httpStatus, String... args) {
        super(String.format("msgCode = %s, args = %s", msgCode, Arrays.toString(args)));
        this.msgCode = msgCode;
        this.httpStatus = httpStatus;
        this.args = args;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String[] getArgs() {
        return args;
    }
}