package com.campus.common.jwt;

import lombok.Data;

import java.util.Set;

@Data
public class TokenInfo {

    private Long userId;
    private String username;
    private Set<String> roles;
    private Set<String> permissions;
    private String accessToken;
    private String refreshToken;
    private Long accessExpireTime;
    private Long refreshExpireTime;
}
