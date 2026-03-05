package com.campus.security.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用状态枚举（角色/用户/菜单等通用）
 * 替代0/1魔法值，提升代码可读性
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum {

    ENABLE(1, "启用"),   // 状态正常，可使用
    DISABLE(0, "禁用");  // 状态异常，不可使用

    /**
     * MyBatis-Plus存储到数据库的数值
     */
    @EnumValue
    private final Integer code;

    /**
     * Jackson序列化返回给前端的描述（如"启用"而非1）
     */
    @JsonValue
    private final String desc;
}