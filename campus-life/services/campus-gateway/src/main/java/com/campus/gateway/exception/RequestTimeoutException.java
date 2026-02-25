package com.campus.gateway.exception;

import com.campus.common.error.CommonErrorCode;

public class RequestTimeoutException extends GatewayException {
    public RequestTimeoutException(String serviceName, long timeout) {
        super(CommonErrorCode.SYSTEM_ERROR,
              String.format("请求服务[%s]超时，超时时间: %dms", serviceName, timeout));
    }
}