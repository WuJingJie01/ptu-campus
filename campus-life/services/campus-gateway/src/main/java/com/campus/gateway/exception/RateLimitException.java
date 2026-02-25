package com.campus.gateway.exception;

import com.campus.common.error.CommonErrorCode;

public class RateLimitException extends GatewayException {
    public RateLimitException(String message) {
        super(CommonErrorCode.FORBIDDEN, message);
    }
}