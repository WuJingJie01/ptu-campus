package com.campus.common.error;

import lombok.Getter;

@Getter
public enum CommonErrorCode implements ErrorCode {

    SUCCESS(0, "成功"),

    /* 参数错误 */
    PARAM_ERROR(100001, "参数错误"),
    PARAM_MISSING(100002, "参数缺失"),

    /* 认证授权 */
    UNAUTHORIZED(300001, "未登录"),
    TOKEN_EXPIRED(300002, "Token已过期"),
    TOKEN_INVALID(300003, "Token无效"),
    FORBIDDEN(300004, "无权限"),

    /* 服务业务 */
    SERVER_ERROR(500000, "服务异常"),
    SERVICE_UNAVAILABLE(500001, "服务不可用"),

    /* 系统异常 */
    SYSTEM_ERROR(900000, "系统异常"),

    /* 用户相关 */
    USER_NOT_FOUND(800001, "用户不存在");

    private final Integer code;
    private final String message;

    CommonErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}