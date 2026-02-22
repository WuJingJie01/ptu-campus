package com.campus.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    // 建议用 Base64 编码后的密钥（更安全）
    private static final String SECRET =
            "Y2FtcHVzLWxpZmUtc3VwZXItc2VjcmV0LWtleS0xMjM0NTY="; // base64

    private static final SecretKey KEY =
            Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

    // 15分钟
    private static final long ACCESS_EXPIRE = 1000 * 60 * 15;

    /**
     * 生成 AccessToken
     */
    public static String generateAccessToken(Long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE))
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析 Token
     */
    public static Long parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }
}