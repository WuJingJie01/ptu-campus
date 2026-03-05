package com.campus.common.exception;

import com.campus.common.error.CommonErrorCode;

public class PermissionException extends BusinessException {

    public PermissionException() {
        super(CommonErrorCode.UNAUTHORIZED);
    }
    
    public PermissionException(String message) {
        super(CommonErrorCode.FORBIDDEN, message);
    }
}
