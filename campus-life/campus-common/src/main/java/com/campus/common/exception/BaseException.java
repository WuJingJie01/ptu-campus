package com.campus.common.exception;


import com.campus.common.error.ErrorCode;
import lombok.Data;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    Integer code;
    String message;

    protected BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    protected BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
        this.message = message;
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }
}