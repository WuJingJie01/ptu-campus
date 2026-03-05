package com.campus.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "Y2FtcHVzLWxpZmUtc3VwZXItc2VjcmV0LWtleS0xMjM0NTY=";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    
    private static final long ACCESS_EXPIRE_MS = 1000 * 60 * 15;
    private static final long REFRESH_EXPIRE_MS = 1000 * 60 * 60 * 12;

    public static String generateAccessToken(Long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "access")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRE_MS))
                .signWith(KEY)
                .compact();
    }

    public static String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "refresh")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRE_MS))
                .signWith(KEY)
                .compact();
    }

    public static Long parseUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }

    public static String getTokenType(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("type", String.class);
    }

    public static boolean isAccessToken(String token) {
        return "access".equals(getTokenType(token));
    }

    public static boolean isRefreshToken(String token) {
        return "refresh".equals(getTokenType(token));
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public static long getAccessExpireSeconds() {
        return ACCESS_EXPIRE_MS / 1000;
    }

    public static long getRefreshExpireSeconds() {
        return REFRESH_EXPIRE_MS / 1000;
    }
}
