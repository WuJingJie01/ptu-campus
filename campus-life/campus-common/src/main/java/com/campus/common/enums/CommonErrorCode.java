package com.campus.common.enums;

public enum CommonErrorCode {

    SUCCESS(0, "成功"),

    PARAM_ERROR(100001, "参数错误"),
    USER_NOT_EXIST(200001, "用户不存在"),
    PASSWORD_ERROR(200002, "密码错误"),

    TOKEN_EXPIRED(300001, "Token已过期"),
    UNAUTHORIZED(300002, "未登录"),

    SYSTEM_ERROR(500000, "系统异常");

    private final Integer code;
    private final String message;

    CommonErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() { return code; }
    public String getMessage() { return message; }
}