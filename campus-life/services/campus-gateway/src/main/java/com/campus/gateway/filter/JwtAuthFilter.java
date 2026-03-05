package com.campus.gateway.filter;

import com.campus.common.constant.AuthConstants;
import com.campus.common.error.CommonErrorCode;
import com.campus.common.jwt.JwtUtil;
import com.campus.common.result.Result;
import com.campus.gateway.config.AuthProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper;
    private final List<String> whiteList;

    public JwtAuthFilter(ObjectMapper objectMapper,
                         AuthProperties authProperties) {
        this.objectMapper = objectMapper;
        this.whiteList = authProperties.getWhiteList().stream()
                .map(String::trim)
                .filter(StringUtils::hasText)
                .distinct()
                .toList();
        log.info("Gateway white list: {}", this.whiteList);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        
        if (isWhiteListed(path)) {
            log.debug("Path {} is in white list, skipping auth", path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(AuthConstants.AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(AuthConstants.BEARER_PREFIX)) {
            log.warn("Missing or invalid Authorization header for path: {}", path);
            return onError(exchange, CommonErrorCode.UNAUTHORIZED);
        }

        String token = authHeader.substring(AuthConstants.BEARER_PREFIX.length());

        if (!JwtUtil.validateToken(token)) {
            log.warn("Invalid JWT token for path: {}", path);
            return onError(exchange, CommonErrorCode.TOKEN_INVALID);
        }

        if (!JwtUtil.isAccessToken(token)) {
            log.warn("Token is not access token for path: {}", path);
            return onError(exchange, CommonErrorCode.TOKEN_INVALID);
        }

        Long userId = JwtUtil.parseUserId(token);

        ServerHttpRequest newRequest = exchange.getRequest().mutate()
                .header(AuthConstants.USER_ID_HEADER, String.valueOf(userId))
                .build();
        
        ServerWebExchange newExchange = exchange.mutate()
                .request(newRequest)
                .build();
        
        log.debug("User {} authenticated successfully for path: {}", userId, path);
        return chain.filter(newExchange);
    }

    private boolean isWhiteListed(String path) {
        for (String pattern : whiteList) {
            if (pathMatcher.match(pattern.trim(), path)) {
                return true;
            }
        }
        return false;
    }

    private Mono<Void> onError(ServerWebExchange exchange, CommonErrorCode errorCode) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        Result<Void> result = Result.fail(errorCode);
        
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(result);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize error response", e);
            String fallbackBody = String.format("{\"code\":%d,\"message\":\"%s\",\"data\":null}", 
                    errorCode.getCode(), errorCode.getMessage());
            DataBuffer buffer = response.bufferFactory().wrap(fallbackBody.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
