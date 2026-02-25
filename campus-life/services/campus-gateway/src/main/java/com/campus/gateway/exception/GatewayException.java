package com.campus.gateway.exception;

import com.campus.common.error.ErrorCode;
import com.campus.common.exception.BaseException;
import lombok.Getter;

@Getter
public class GatewayException extends BaseException {
    private final ErrorCode errorCode;

    public GatewayException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
    public GatewayException(ErrorCode errorCode, String message) {
        super(errorCode, message);
        this.errorCode = errorCode;
    }
}