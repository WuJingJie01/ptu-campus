package com.campus.user.handler;

import com.campus.common.error.ErrorCode;
import com.campus.common.result.Result;
import com.campus.common.exception.BusinessException;
import com.campus.common.error.CommonErrorCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常 code={} message={}", e.getCode(), e.getMessage());

        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * @RequestBody 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        String message = Objects.requireNonNull(e.getBindingResult()
                        .getFieldError())
                .getDefaultMessage();

        log.warn("参数校验异常: {}", message);

        return Result.fail(
                CommonErrorCode.PARAM_ERROR.getCode(),
                message
        );
    }

    /**
     * 表单参数校验异常
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {

        String message = Objects.requireNonNull(e.getBindingResult()
                        .getFieldError())
                .getDefaultMessage();

        log.warn("绑定异常: {}", message);

        return Result.fail(
                CommonErrorCode.PARAM_ERROR.getCode(),
                message
        );
    }

    /**
     * 单参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleConstraintViolationException(
            ConstraintViolationException e) {

        log.warn("约束异常: {}", e.getMessage());

        return Result.fail(
                CommonErrorCode.PARAM_ERROR.getCode(),
                e.getMessage()
        );
    }

    /**
     * 系统异常兜底
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {

        log.error("系统异常：", e);

        return Result.fail(
                CommonErrorCode.SYSTEM_ERROR.getCode(),
                CommonErrorCode.SYSTEM_ERROR.getMessage()
        );
    }
}