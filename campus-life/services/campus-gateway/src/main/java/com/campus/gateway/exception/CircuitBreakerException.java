package com.campus.gateway.exception;

import com.campus.common.error.CommonErrorCode;

public class CircuitBreakerException extends GatewayException {
    public CircuitBreakerException(String serviceName) {
        super(CommonErrorCode.SERVICE_UNAVAILABLE,
              String.format("服务[%s]熔断，请稍后重试", serviceName));
    }
}