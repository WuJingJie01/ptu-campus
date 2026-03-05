package com.campus.security.service;

import com.campus.security.entity.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

@Slf4j
public class PermissionService {

    public boolean hasPermission(String permission) {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return false;
        }
        Set<String> permissions = loginUser.getPermissions();
        return permissions != null && permissions.contains(permission);
    }

    public boolean hasAnyPermission(String... permissions) {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return false;
        }
        Set<String> userPermissions = loginUser.getPermissions();
        if (userPermissions == null) {
            return false;
        }
        for (String permission : permissions) {
            if (userPermissions.contains(permission)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRole(String role) {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return false;
        }
        Set<String> roles = loginUser.getRoles();
        return roles != null && roles.contains(role);
    }

    public boolean hasAnyRole(String... roles) {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return false;
        }
        Set<String> userRoles = loginUser.getRoles();
        if (userRoles == null) {
            return false;
        }
        for (String role : roles) {
            if (userRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkSelf(Object resourceId, String resource) {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return false;
        }
        
        if (resourceId == null) {
            return false;
        }
        
        Long currentUserId = loginUser.getUserId();
        Long targetId = null;
        
        if (resourceId instanceof Long) {
            targetId = (Long) resourceId;
        } else if (resourceId instanceof Integer) {
            targetId = ((Integer) resourceId).longValue();
        } else if (resourceId instanceof String) {
            try {
                targetId = Long.parseLong((String) resourceId);
            } catch (NumberFormatException e) {
                log.warn("Failed to parse resourceId: {}", resourceId);
                return false;
            }
        }
        
        if (targetId == null) {
            return false;
        }
        
        return currentUserId.equals(targetId);
    }

    public boolean checkSelf(Object[] args, String resource, Object principal) {
        if (args == null || args.length == 0) {
            return false;
        }
        
        for (Object arg : args) {
            if (arg instanceof Long || arg instanceof Integer || arg instanceof String) {
                return checkSelf(arg, resource);
            }
        }
        
        return false;
    }

    public Long getCurrentUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUserId() : null;
    }

    public String getCurrentUsername() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUsername() : null;
    }

    public LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof LoginUser) {
            return (LoginUser) principal;
        }
        return null;
    }
}
