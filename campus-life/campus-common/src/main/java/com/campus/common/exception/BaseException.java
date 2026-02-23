package com.campus.common.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final Integer code;

    protected BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}