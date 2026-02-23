package com.campus.common.error;

import lombok.Getter;

@Getter
public enum CommonErrorCode implements ErrorCode {

    SUCCESS(0, "成功"),

    PARAM_ERROR(100001, "参数错误"),
    PARAM_MISSING(100002, "参数缺失"),

    UNAUTHORIZED(300001, "未登录"),
    TOKEN_EXPIRED(300002, "Token已过期"),

    SYSTEM_ERROR(900000, "系统异常"),

    USER_NOT_FOUND(800001, "用户不存在");

    private final Integer code;
    private final String message;

    CommonErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}