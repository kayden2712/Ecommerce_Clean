package com.example.ecommerce_clean.shared.enums;

public enum PaymentStatus {
    INITIATED,      // tạo record
    PENDING,        // chờ gateway
    SUCCESS,        // thanh toán thành công
    FAILED,         // thanh toán thất bại
    CANCELLED,      // user hủy
    EXPIRED         // quá thời gian
}
