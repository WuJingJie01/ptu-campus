package com.campus.gateway.exception;

import com.campus.gateway.common.ErrorCode;
import lombok.Getter;

@Getter
public class GatewayException extends RuntimeException {

    private final ErrorCode errorCode;

    public GatewayException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}