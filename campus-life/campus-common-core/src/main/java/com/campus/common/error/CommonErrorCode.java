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
    ACCESS_DENIED(300005, "访问被拒绝"),
    PASSWORD_ERROR(300006, "密码错误"),
    ACCOUNT_DISABLED(300007, "账号已被禁用"),
    LOGIN_EXPIRED(300008, "登录已过期"),

    /* 服务业务 */
    SERVER_ERROR(500000, "服务异常"),
    SERVICE_UNAVAILABLE(500001, "服务不可用"),

    /* 系统异常 */
    SYSTEM_ERROR(900000, "系统异常"),

    /* 用户相关 */
    USER_NOT_FOUND(800001, "用户不存在"),
    USERNAME_EXISTS(800002, "用户名已存在"),
    EMAIL_EXISTS(800003, "邮箱已被注册"),
    PHONE_EXISTS(800004, "手机号已被注册");

    private final Integer code;
    private final String message;

    CommonErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
