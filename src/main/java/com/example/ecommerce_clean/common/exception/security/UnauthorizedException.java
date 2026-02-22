package com.example.ecommerce_clean.common.exception.security;

import com.example.ecommerce_clean.common.exception.base.BusinessException;
import com.example.ecommerce_clean.shared.enums.ErrorCode;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
