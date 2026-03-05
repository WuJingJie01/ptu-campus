package com.campus.security.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.campus.security.enums.CommonStatusEnum;
import com.campus.security.enums.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统用户DO（数据库对象）
 * 严格对应 sys_user 表，不包含业务逻辑
 */
@Data // 包含 getter/setter/toString/equals/hashCode，简单DO可使用
@NoArgsConstructor // 生成无参构造（MyBatis-Plus必须）
@AllArgsConstructor // 生成全参构造（可选，方便测试）
@TableName("sys_user") // 指定数据库表名
public class SysUser implements Serializable { // 实现序列化接口（分布式场景必备）

    private static final long serialVersionUID = 1L; // 序列化ID，避免反序列化失败

    /**
     * 主键ID（自增）
     */
    @TableId(type = IdType.AUTO) // MyBatis-Plus主键自增策略
    @Getter(onMethod_ = @JsonIgnore) // 仅禁止序列化ID的getter（可选，视业务需求）
    private Long id;

    /**
     * 用户名（唯一）
     */
    @TableField(value = "username", condition = SqlCondition.EQUAL) // 指定数据库字段，查询时用精确匹配
    private String username;

    /**
     * 密码（加密存储，禁止序列化返回前端）
     */
    @TableField(value = "password") // 查询时不返回该字段（MyBatis-Plus层面）
    private String password;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 邮箱（唯一）
     */
    @TableField("email")
    private String email;

    /**
     * 手机号（可选）
     */
    @TableField("phone")
    private String phone;

    /**
     * 头像URL
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 用户状态（枚举替代魔法值）
     */
    @TableField("status")
    private CommonStatusEnum status; // 用枚举替代Integer，避免0/1魔法值

    /**
     * 逻辑删除标识（0-未删除，1-已删除）
     */
    @TableLogic // MyBatis-Plus逻辑删除注解，查询时自动过滤已删除数据
    @TableField("deleted")
    @JsonIgnore // 禁止返回给前端
    private Integer deleted;

    /**
     * 创建时间（插入时自动填充）
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT) // 插入时自动填充
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 统一日期格式返回前端
    private LocalDateTime createTime;

    /**
     * 更新时间（插入和更新时自动填充）
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE) // 插入和更新时自动填充
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}