package com.campus.gateway.exception;

import com.campus.common.error.CommonErrorCode;

// 2. 路由相关异常
public class RouteNotFoundException extends GatewayException {
    public RouteNotFoundException(String routeId) {
        super(CommonErrorCode.SYSTEM_ERROR,
              String.format("路由[%s]不存在或未配置", routeId));
    }
}