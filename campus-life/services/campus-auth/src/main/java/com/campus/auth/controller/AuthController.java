package com.campus.auth.controller;

import com.campus.auth.dto.LoginRequest;
import com.campus.auth.dto.RefreshTokenRequest;
import com.campus.auth.dto.RegisterRequest;
import com.campus.auth.dto.TokenResponse;
import com.campus.auth.service.AuthService;
import com.campus.common.jwt.JwtUtil;
import com.campus.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Result<TokenResponse> register(@RequestBody @Valid RegisterRequest request) {
        log.info("Register request for username: {}", request.getUsername());
        TokenResponse response = authService.register(request);
        return Result.success(response);
    }

    @PostMapping("/login")
    public Result<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        log.info("Login request for username: {}", request.getUsername());
        TokenResponse response = authService.login(request);
        return Result.success(response);
    }

    @PostMapping("/refresh")
    public Result<TokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        log.info("Refresh token request");
        TokenResponse response = authService.refreshToken(request.getRefreshToken());
        return Result.success(response);
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("X-User-Id") Long userId) {
        log.info("Logout request for userId: {}", userId);
        authService.logout(userId);
        return Result.success(null);
    }

    @GetMapping("/check")
    public Result<Void> checkAuth(@RequestHeader(value = "X-User-Id", required = false) Long userId) {
        if (userId == null) {
            return Result.fail(com.campus.common.error.CommonErrorCode.UNAUTHORIZED);
        }
        return Result.success(null);
    }
}
