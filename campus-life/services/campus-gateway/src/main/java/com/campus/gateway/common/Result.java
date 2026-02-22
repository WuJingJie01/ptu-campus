package com.campus.gateway.common;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Result<T> {

    private Integer code;
    private String message;
    private T data;
    private String traceId;
    private LocalDateTime timestamp;

    public static <T> Result<T> success(T data, String traceId) {
        return Result.<T>builder()
                .code(200)
                .message("SUCCESS")
                .data(data)
                .traceId(traceId)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> Result<T> fail(ErrorCode errorCode, String traceId) {
        return Result.<T>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(null)
                .traceId(traceId)
                .timestamp(LocalDateTime.now())
                .build();
    }
}