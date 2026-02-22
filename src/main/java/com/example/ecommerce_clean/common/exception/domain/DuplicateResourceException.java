package com.example.ecommerce_clean.common.exception.domain;

import com.example.ecommerce_clean.common.exception.base.BusinessException;
import com.example.ecommerce_clean.shared.enums.ErrorCode;

public class DuplicateResourceException extends BusinessException {
    public DuplicateResourceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
