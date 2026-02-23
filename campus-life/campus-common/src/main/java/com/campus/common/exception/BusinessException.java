package com.campus.common.exception;

import com.campus.common.error.ErrorCode;

public class BusinessException extends BaseException {

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getMessage());
    }

    public BusinessException(Integer code, String message) {
        super(code, message);
    }
}