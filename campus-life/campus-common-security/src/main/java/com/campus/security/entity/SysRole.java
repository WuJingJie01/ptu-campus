package com.campus.security.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.campus.security.enums.CommonStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统角色DO（数据库对象）
 * 严格对应 sys_role 表，仅做数据库映射，无业务逻辑
 */
@Data // 基础getter/setter/toString等，简单DO可直接使用
@NoArgsConstructor // MyBatis-Plus反射创建对象必需无参构造
@AllArgsConstructor // 全参构造，方便测试/批量创建
@TableName("sys_role") // 指定数据库表名，避免默认驼峰转换问题
public class SysRole implements Serializable { // 分布式场景必需序列化

    private static final long serialVersionUID = 1L; // 显式序列化ID，避免反序列化失败

    /**
     * 角色主键ID（自增）
     */
    @TableId(type = IdType.AUTO) // MyBatis-Plus自增主键策略
    private Long id;

    /**
     * 角色编码（唯一，如ADMIN/USER/AUDITOR）
     * 建议全大写，作为权限标识核心
     */
    @TableField(value = "role_code", condition = SqlCondition.EQUAL) // 查询时精确匹配
    private String roleCode;

    /**
     * 角色名称（如超级管理员/普通用户/审核员）
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 父角色ID（用于角色层级，如管理员→子管理员）
     * 0表示顶级角色
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 排序序号（用于前端展示排序）
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 角色状态（启用/禁用）
     * 用枚举替代魔法值，避免0/1散落在代码中
     */
    @TableField("status")
    private CommonStatusEnum status;

    /**
     * 逻辑删除标识（0-未删除，1-已删除）
     * 角色数据不可物理删除，仅逻辑删除
     */
    @TableLogic // MyBatis-Plus逻辑删除注解，查询自动过滤已删除数据
    @TableField("deleted")
    @JsonIgnore // 序列化时忽略，禁止返回给前端
    private Integer deleted;

    /**
     * 创建时间（插入时自动填充）
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT) // 插入自动填充
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 统一日期格式返回前端
    private LocalDateTime createTime;

    /**
     * 更新时间（插入/更新时自动填充）
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE) // 插入/更新自动填充
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}