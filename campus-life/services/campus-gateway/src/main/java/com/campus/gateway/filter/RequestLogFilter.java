package com.campus.gateway.filter;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class RequestLogFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        exchange.getAttributes().put("traceId", traceId);

        long start = System.currentTimeMillis();

        return chain.filter(exchange)
                .doFinally(signal -> {
                    long time = System.currentTimeMillis() - start;

                    System.out.println(
                            "TRACE_ID=" + traceId +
                            " PATH=" + exchange.getRequest().getURI() +
                            " COST=" + time + "ms"
                    );

                    MDC.clear();
                });
    }

    @Override
    public int getOrder() {
        return -200;
    }
}