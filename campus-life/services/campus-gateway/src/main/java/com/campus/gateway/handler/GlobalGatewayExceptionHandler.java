package com.campus.gateway.handler;

import com.campus.common.error.CommonErrorCode;
import com.campus.common.result.Result;
import com.campus.gateway.exception.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Gateway全局异常处理器
 * 优先级: -1 确保最先执行
 */
@Slf4j
@Order(-1)
@Configuration
@RequiredArgsConstructor
public class GlobalGatewayExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        
        // 1. 设置默认响应状态码和Content-Type
        if (response.getStatusCode() == null) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 2. 构建统一响应结果
        Result<?> result = buildErrorResult(ex);
        
        // 3. 根据异常类型记录不同级别的日志
        logException(ex, result);

        // 4. 序列化并返回
        return writeResponse(response, result);
    }

    /**
     * 构建统一的错误响应结果
     */
    private Result<?> buildErrorResult(Throwable ex) {
        // 处理自定义网关异常
        if (ex instanceof GatewayException gatewayEx) {
            return Result.fail(
                gatewayEx.getErrorCode().getCode(),
                gatewayEx.getMessage()
            );
        }
        
        // 处理Spring Cloud Gateway原生异常
        if (ex instanceof NotFoundException) {
            return Result.fail(
                CommonErrorCode.SYSTEM_ERROR.getCode(),
                "服务路由未找到: " + ex.getMessage()
            );
        }
        
        if (ex instanceof TimeoutException) {
            return Result.fail(
                CommonErrorCode.SYSTEM_ERROR.getCode(),
                "请求超时，请稍后重试"
            );
        }
        
        if (ex instanceof ResponseStatusException) {
            HttpStatus status = (HttpStatus) ((ResponseStatusException) ex).getStatusCode();
            String message = ((ResponseStatusException) ex).getReason();
            
            if (status == HttpStatus.NOT_FOUND) {
                return Result.fail(
                    CommonErrorCode.SYSTEM_ERROR.getCode(),
                    "请求资源不存在"
                );
            }
            
            if (status == HttpStatus.SERVICE_UNAVAILABLE) {
                return Result.fail(
                    CommonErrorCode.SYSTEM_ERROR.getCode(),
                    "服务暂时不可用"
                );
            }
            
            return Result.fail(
                status.value(),
                message != null ? message : status.getReasonPhrase()
            );
        }
        
        // 处理业务异常（从下游服务传播上来的）
        if (ex.getCause() != null) {
            Throwable cause = ex.getCause();
            
            // 处理JSON解析异常（可能是下游服务返回格式错误）
            if (cause instanceof JsonProcessingException) {
                return Result.fail(
                    CommonErrorCode.SYSTEM_ERROR.getCode(),
                    "服务响应格式错误"
                );
            }
            
            // 处理连接异常
            if (cause instanceof java.net.ConnectException) {
                return Result.fail(
                    CommonErrorCode.SYSTEM_ERROR.getCode(),
                    "服务连接失败，请检查服务状态"
                );
            }
            
            // 处理SSL握手异常
            if (cause instanceof javax.net.ssl.SSLHandshakeException) {
                return Result.fail(
                    CommonErrorCode.SYSTEM_ERROR.getCode(),
                    "安全连接失败"
                );
            }
        }
        
        // 默认系统异常
        return Result.fail(
            CommonErrorCode.SYSTEM_ERROR.getCode(),
            "网关内部错误，请联系管理员"
        );
    }

    /**
     * 异常日志记录策略
     */
    private void logException(Throwable ex, Result<?> result) {
        String errorCode = String.valueOf(result.getCode());
        
        // 4系列错误（客户端错误）- WARN级别
        if (errorCode.startsWith("4")) {
            log.warn("客户端异常: code={}, message={}, exception={}", 
                errorCode, result.getMessage(), ex.getClass().getSimpleName());
        }
        // 5系列错误（服务端错误）- ERROR级别
        else if (errorCode.startsWith("5") || errorCode.startsWith("9")) {
            log.error("服务端异常: code={}, message={}", 
                errorCode, result.getMessage(), ex);
        }
        // 3系列错误（认证授权）- INFO级别
        else if (errorCode.startsWith("3")) {
            log.info("认证授权异常: code={}, message={}", 
                errorCode, result.getMessage());
        }
        // 其他错误 - WARN级别
        else {
            log.warn("业务异常: code={}, message={}", 
                errorCode, result.getMessage(), ex);
        }
    }

    /**
     * 写入响应
     */
    private Mono<Void> writeResponse(ServerHttpResponse response, Result<?> result) {
        return Mono.defer(() -> {
            try {
                DataBufferFactory bufferFactory = response.bufferFactory();
                String jsonResult = objectMapper.writeValueAsString(result);
                DataBuffer dataBuffer = bufferFactory.wrap(
                    jsonResult.getBytes(StandardCharsets.UTF_8)
                );
                return response.writeWith(Mono.just(dataBuffer));
            } catch (JsonProcessingException e) {
                log.error("序列化异常结果失败", e);
                return response.setComplete();
            }
        });
    }
}