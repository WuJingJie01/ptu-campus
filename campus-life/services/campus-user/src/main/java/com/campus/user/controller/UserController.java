package com.campus.user.controller;

import com.alibaba.nacos.api.model.v2.Result;

import com.campus.user.service.UserService;
import com.campus.user.vo.UserVO;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/info")
    public Result<UserVO> getUserInfo() {
        return Result.success(userService.getUserInfo());
    }
}