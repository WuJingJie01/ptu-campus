package com.campus.gateway.filter;


import com.campus.common.error.CommonErrorCode;
import com.campus.common.exception.AuthException;
import com.campus.gateway.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // 放行登录接口
        if (path.startsWith("/auth/login")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthException(CommonErrorCode.UNAUTHORIZED);
        }

        String token = authHeader.replace("Bearer ", "");

        try {
            Long userId = JwtUtil.parseToken(token);

            ServerWebExchange newExchange = exchange.mutate()
                    .request(builder ->
                            builder.header("X-User-Id", String.valueOf(userId)))
                    .build();

            return chain.filter(newExchange);

        } catch (Exception e) {
            throw new AuthException(CommonErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}