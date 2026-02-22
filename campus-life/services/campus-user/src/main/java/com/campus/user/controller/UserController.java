package com.campus.user.controller;

import com.alibaba.nacos.api.model.v2.Result;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/info")
    public Result<?> userInfo() {
        Map<String, Object> user = new HashMap<>();
        user.put("id", 1);
        user.put("name", "张三");
        user.put("avatar", "https://avatar.com/1");
        return Result.success(user);
    }
}