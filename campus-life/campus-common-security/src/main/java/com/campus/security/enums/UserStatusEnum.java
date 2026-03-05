package com.campus.security.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户状态枚举（替代魔法值0/1）
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    ENABLE(1, "启用"),
    DISABLE(0, "禁用");

    /**
     * MyBatis-Plus存储到数据库的值
     */
    @EnumValue
    private final Integer code;

    /**
     * Jackson返回给前端的描述
     */
    @JsonValue
    private final String desc;
}