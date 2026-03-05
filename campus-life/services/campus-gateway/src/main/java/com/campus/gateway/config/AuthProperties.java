package com.campus.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 认证相关配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "auth") // 配置前缀
public class AuthProperties {
    /**
     * 网关白名单路径
     */
    private List<String> whiteList = List.of(); // 默认空列表
}