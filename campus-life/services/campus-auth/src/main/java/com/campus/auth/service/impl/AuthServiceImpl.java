package com.campus.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.auth.dto.LoginRequest;
import com.campus.auth.dto.RegisterRequest;
import com.campus.auth.dto.TokenResponse;
import com.campus.auth.service.AuthService;
import com.campus.common.error.CommonErrorCode;
import com.campus.common.exception.AuthException;
import com.campus.common.exception.BusinessException;
import com.campus.common.jwt.JwtUtil;
import com.campus.common.redis.AuthRedisUtil;
import com.campus.security.entity.SysUser;
import com.campus.security.entity.SysUserRole;
import com.campus.security.enums.CommonStatusEnum;
import com.campus.security.mapper.SysUserMapper;
import com.campus.security.mapper.SysUserRoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Long DEFAULT_ROLE_ID = 2L;

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public TokenResponse login(LoginRequest request) {
        SysUser user = findUserByUsername(request.getUsername());
        
        if (user == null) {
            throw new AuthException(CommonErrorCode.USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException(CommonErrorCode.PASSWORD_ERROR);
        }

        if (user.getStatus() == CommonStatusEnum.DISABLE) {
            throw new AuthException(CommonErrorCode.ACCOUNT_DISABLED);
        }

        return generateAndStoreTokens(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TokenResponse register(RegisterRequest request) {
        SysUser existingUser = findUserByUsername(request.getUsername());
        if (existingUser != null) {
            throw new BusinessException(CommonErrorCode.USERNAME_EXISTS);
        }

        SysUser newUser = new SysUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPhone(request.getPhone());
        newUser.setStatus(CommonStatusEnum.ENABLE);

        sysUserMapper.insert(newUser);

        assignDefaultRole(newUser.getId());

        log.info("User registered successfully: {}", newUser.getUsername());
        
        return generateAndStoreTokens(newUser);
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if (!JwtUtil.validateToken(refreshToken)) {
            throw new AuthException(CommonErrorCode.TOKEN_INVALID);
        }

        if (!JwtUtil.isRefreshToken(refreshToken)) {
            throw new AuthException(CommonErrorCode.TOKEN_INVALID);
        }

        Long userId = JwtUtil.parseUserId(refreshToken);

        AuthRedisUtil redisUtil = new AuthRedisUtil(redisTemplate);
        if (!redisUtil.validateRefreshToken(userId, refreshToken)) {
            throw new AuthException(CommonErrorCode.TOKEN_EXPIRED);
        }

        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new AuthException(CommonErrorCode.USER_NOT_FOUND);
        }

        return generateAndStoreTokens(user);
    }

    @Override
    public void logout(Long userId) {
        AuthRedisUtil redisUtil = new AuthRedisUtil(redisTemplate);
        redisUtil.removeRefreshToken(userId);
        log.info("User logged out: {}", userId);
    }

    private SysUser findUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        return sysUserMapper.selectOne(queryWrapper);
    }

    private TokenResponse generateAndStoreTokens(SysUser user) {
        Long userId = user.getId();
        
        String accessToken = JwtUtil.generateAccessToken(userId);
        String refreshToken = JwtUtil.generateRefreshToken(userId);

        AuthRedisUtil redisUtil = new AuthRedisUtil(redisTemplate);
        
        redisUtil.storeRefreshToken(userId, refreshToken, JwtUtil.getRefreshExpireSeconds());

        log.debug("Tokens generated for user: {}", userId);

        return TokenResponse.of(
                accessToken,
                refreshToken,
                JwtUtil.getAccessExpireSeconds(),
                JwtUtil.getRefreshExpireSeconds(),
                user.getUsername(),
                userId
        );
    }

    private void assignDefaultRole(Long userId) {
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(DEFAULT_ROLE_ID);
        sysUserRoleMapper.insert(userRole);
        log.info("Assigned default USER role to user: {}", userId);
    }
}
