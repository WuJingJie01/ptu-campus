package com.campus.common.result;

import lombok.Data;
import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    private Result() {}

    // 成功
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 0;
        result.message = "success";
        result.data = data;
        result.timestamp = System.currentTimeMillis();
        return result;
    }

    // 失败
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.code = code;
        result.message = message;
        result.timestamp = System.currentTimeMillis();
        return result;
    }
}