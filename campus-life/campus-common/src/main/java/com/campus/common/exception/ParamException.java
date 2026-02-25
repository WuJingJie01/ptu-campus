package com.campus.common.exception;

import com.campus.common.error.CommonErrorCode;

public class ParamException extends BusinessException {

    public ParamException(String message) {
        super(CommonErrorCode.PARAM_ERROR);
    }
}