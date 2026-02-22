package com.campus.gateway.exception;

import com.campus.gateway.common.ErrorCode;
import com.campus.gateway.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Configuration
@Order(-1)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        ErrorCode errorCode = ErrorCode.SERVER_ERROR;

        if (ex instanceof GatewayException gatewayException) {
            errorCode = gatewayException.getErrorCode();
        }

        Result<Object> result = Result.fail(errorCode, traceId);

        exchange.getResponse().getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(result);

            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse()
                            .bufferFactory()
                            .wrap(bytes))
            );
        } catch (Exception e) {
            return exchange.getResponse().setComplete();
        }
    }
}