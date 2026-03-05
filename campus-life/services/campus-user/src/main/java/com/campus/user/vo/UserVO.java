package com.campus.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter // 如果接收参数，则需要Setter；如果仅作为响应，可考虑移除Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L; // 序列化ID，避免反序列化失败

    private String nickname;

    private String avatar;

    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") // 简单手机号格式校验，用于接收参数时
    private String phone; // 返回前端时，应在业务逻辑层或getter中脱敏

    private String username;
}