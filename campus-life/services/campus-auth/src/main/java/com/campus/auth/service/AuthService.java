package com.campus.auth.service;

import com.campus.auth.dto.LoginRequest;
import com.campus.auth.dto.RegisterRequest;
import com.campus.auth.dto.TokenResponse;

public interface AuthService {

    TokenResponse login(LoginRequest request);

    TokenResponse register(RegisterRequest request);

    TokenResponse refreshToken(String refreshToken);

    void logout(Long userId);
}
