package com.campus.gateway.filter;

import com.campus.common.error.CommonErrorCode;
import com.campus.common.result.Result;
import com.campus.gateway.exception.GatewayException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 过滤器异常处理全局拦截器
 * 优先级最高，用于捕获过滤器链中的异常
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FilterExceptionHandlerFilter implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
            .onErrorResume(throwable -> {
                // 处理过滤器链中的异常
                return handleFilterException(exchange, throwable);
            });
    }

    private Mono<Void> handleFilterException(ServerWebExchange exchange, Throwable throwable) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        
        Result<?> result;
        
        if (throwable instanceof GatewayException) {
            GatewayException ex = (GatewayException) throwable;
            result = Result.fail(
                ex.getErrorCode().getCode(),
                ex.getMessage()
            );
            log.warn("网关业务异常: {}", ex.getMessage());
        } 
        else if (throwable instanceof IllegalArgumentException) {
            result = Result.fail(
                CommonErrorCode.PARAM_ERROR.getCode(),
                "请求参数错误: " + throwable.getMessage()
            );
            log.warn("参数验证异常: {}", throwable.getMessage());
        }
        else {
            result = Result.fail(
                CommonErrorCode.SYSTEM_ERROR.getCode(),
                "网关过滤器异常"
            );
            log.error("过滤器异常: ", throwable);
        }
        
        try {
            DataBuffer buffer = response.bufferFactory()
                .wrap(objectMapper.writeValueAsString(result)
                    .getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("异常响应序列化失败", e);
            return response.setComplete();
        }
    }

    @Override
    public int getOrder() {
        // 设置最高优先级，确保最先执行
        return Ordered.HIGHEST_PRECEDENCE;
    }
}