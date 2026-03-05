package com.campus.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.common.error.CommonErrorCode;
import com.campus.security.entity.LoginUser;
import com.campus.security.entity.SysUser;
import com.campus.security.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String USER_ROLE_KEY_PREFIX = "user:role:";
    private static final String USER_PERMISSION_KEY_PREFIX = "user:permission:";
    private static final long CACHE_EXPIRE_HOURS = 30;

    public CustomUserDetailsService(SysUserMapper sysUserMapper, RedisTemplate<String, Object> redisTemplate) {
        this.sysUserMapper = sysUserMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);

        if (sysUser == null) {
            throw new UsernameNotFoundException(CommonErrorCode.USER_NOT_FOUND.getMessage());
        }

        return buildLoginUser(sysUser);
    }

    public LoginUser loadUserByUserId(Long userId) {
        String roleKey = USER_ROLE_KEY_PREFIX + userId;
        String permissionKey = USER_PERMISSION_KEY_PREFIX + userId;

        @SuppressWarnings("unchecked")
        Set<String> roles = (Set<String>) redisTemplate.opsForValue().get(roleKey);
        @SuppressWarnings("unchecked")
        Set<String> permissions = (Set<String>) redisTemplate.opsForValue().get(permissionKey);

        if (roles == null || permissions == null) {
            roles = sysUserMapper.selectRoleCodesByUserId(userId);
            permissions = sysUserMapper.selectPermissionCodesByUserId(userId);

            redisTemplate.opsForValue().set(roleKey, roles, CACHE_EXPIRE_HOURS, TimeUnit.MINUTES);
            redisTemplate.opsForValue().set(permissionKey, permissions, CACHE_EXPIRE_HOURS, TimeUnit.MINUTES);
        }

        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            throw new UsernameNotFoundException(CommonErrorCode.USER_NOT_FOUND.getMessage());
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userId);
        loginUser.setUsername(sysUser.getUsername());
        loginUser.setPassword(sysUser.getPassword());
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);

        return loginUser;
    }

    private LoginUser buildLoginUser(SysUser sysUser) {
        Long userId = sysUser.getId();

        Set<String> roles = sysUserMapper.selectRoleCodesByUserId(userId);
        Set<String> permissions = sysUserMapper.selectPermissionCodesByUserId(userId);

        String roleKey = USER_ROLE_KEY_PREFIX + userId;
        String permissionKey = USER_PERMISSION_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(roleKey, roles, CACHE_EXPIRE_HOURS, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(permissionKey, permissions, CACHE_EXPIRE_HOURS, TimeUnit.MINUTES);

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(userId);
        loginUser.setUsername(sysUser.getUsername());
        loginUser.setPassword(sysUser.getPassword());
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);

        return loginUser;
    }

    public void clearUserCache(Long userId) {
        String roleKey = USER_ROLE_KEY_PREFIX + userId;
        String permissionKey = USER_PERMISSION_KEY_PREFIX + userId;
        redisTemplate.delete(roleKey);
        redisTemplate.delete(permissionKey);
        log.info("Cleared permission cache for user: {}", userId);
    }
}
