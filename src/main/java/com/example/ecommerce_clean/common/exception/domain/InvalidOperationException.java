package com.example.ecommerce_clean.common.exception.domain;

import com.example.ecommerce_clean.common.exception.base.BusinessException;
import com.example.ecommerce_clean.shared.enums.ErrorCode;

public class InvalidOperationException extends BusinessException {
    public InvalidOperationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
