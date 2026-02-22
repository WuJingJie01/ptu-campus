package com.campus.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS(200, "成功"),

    UNAUTHORIZED(401, "未登录"),
    TOKEN_EXPIRED(402, "Token已过期"),
    FORBIDDEN(403, "无权限访问"),

    BAD_REQUEST(400, "参数错误"),
    SERVER_ERROR(500, "服务器异常");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}