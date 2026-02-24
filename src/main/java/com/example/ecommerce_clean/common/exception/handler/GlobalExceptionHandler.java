package com.example.ecommerce_clean.common.exception.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.ecommerce_clean.common.exception.base.BusinessException;
import com.example.ecommerce_clean.shared.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý ngoại lệ nghiệp vụ (BusinessException).
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getErrorCode().getStatus(), ex.getMessage(), request.getRequestURI());
    }

    // * Xử lý ngoại lệ xác thực dữ liệu đầu vào (Validation).

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        // Lấy thông điệp lỗi đầu tiên từ danh sách lỗi xác thực
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    // * Xử lý ngoại lệ tham số không hợp lệ (IllegalArgumentException).

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    // * Xử lý ngoại lệ trạng thái không hợp lệ (IllegalStateException).

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    // * Xử lý tất cả các ngoại lệ chung không được bắt bởi các handler khác.

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred",
                request.getRequestURI());
    }

    // * Phương thức tiện ích để xây dựng phản hồi lỗi thống nhất.

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, String path) {
        ErrorResponse error = ErrorResponse.builder()
                .status(status.value()) // Mã trạng thái HTTP (ví dụ: 400, 500)
                .message(message) // Thông điệp lỗi chi tiết
                .error(status.getReasonPhrase()) // Tên loại lỗi (ví dụ: "Bad Request")
                .timestamp(LocalDateTime.now()) // Thời điểm xảy ra lỗi
                .path(path) // Đường dẫn API gây ra lỗi
                .build();
        return new ResponseEntity<>(error, status);
    }
}
