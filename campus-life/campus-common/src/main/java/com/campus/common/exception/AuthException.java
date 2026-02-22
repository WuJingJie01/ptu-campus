package com.campus.common.exception;

import com.campus.common.enums.CommonErrorCode;

public class AuthException extends BusinessException {

    public AuthException(CommonErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getMessage());
    }
}