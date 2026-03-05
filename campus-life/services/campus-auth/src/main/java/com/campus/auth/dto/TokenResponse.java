package com.campus.auth.dto;

import lombok.Data;

@Data
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
    private Long accessExpireIn;
    private Long refreshExpireIn;
    private String username;
    private Long userId;

    public static TokenResponse of(String accessToken, String refreshToken, 
                                    Long accessExpireIn, Long refreshExpireIn,
                                    String username, Long userId) {
        TokenResponse response = new TokenResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        response.setAccessExpireIn(accessExpireIn);
        response.setRefreshExpireIn(refreshExpireIn);
        response.setUsername(username);
        response.setUserId(userId);
        return response;
    }
}
