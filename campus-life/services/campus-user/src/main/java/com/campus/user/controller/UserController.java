package com.campus.user.controller;

import com.campus.common.result.Result;
import com.campus.security.annotation.SelfPermission;
import com.campus.security.service.PermissionService;
import com.campus.user.service.UserService;
import com.campus.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PermissionService permissionService;

    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        Long userId = permissionService.getCurrentUserId();
        return Result.success(userService.getUserInfo(userId));
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> listUsers() {
        return Result.success(userService.listUsers());
    }

    @PutMapping("/info/self")
    @SelfPermission(resource = "user")
    public Result<?> updateSelfInfo(Long userId, @RequestBody UserVO userVO) {
        userService.updateUserInfo(userId, userVO);
        return Result.success(null);
    }

    @PutMapping("/info/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> updateUserInfo(@PathVariable Long userId, @RequestBody UserVO userVO) {
        userService.updateUserInfo(userId, userVO);
        return Result.success(null);
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> adminDashboard() {
        return Result.success("Admin Dashboard Data");
    }
}
