package com.campus.auth.controller;

import com.alibaba.nacos.api.model.v2.Result;
import com.campus.auth.dto.LoginRequest;
import com.campus.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginRequest request) {

        String email = request.getEmail();

        Long userId = 1L;

        String accessToken = JwtUtil.generateAccessToken(userId);
        String refreshToken = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
                "refresh:" + refreshToken,
                userId.toString(),
                7, TimeUnit.DAYS
        );

        Map<String, String> data = new HashMap<>();
        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);

        return Result.success(data);
    }
}