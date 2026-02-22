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

    /**
     * Xử lý ngoại lệ nghiệp vụ (BusinessException).
     * Được ném ra khi vi phạm các quy tắc nghiệp vụ trong hệ thống.
     *
     * @param ex      ngoại lệ nghiệp vụ chứa mã lỗi và thông điệp
     * @param request yêu cầu HTTP hiện tại
     * @return phản hồi lỗi với mã trạng thái tương ứng
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getErrorCode().getStatus(), ex.getMessage(), request.getRequestURI());
    }

    /**
     * Xử lý ngoại lệ xác thực dữ liệu đầu vào (Validation).
     * Được kích hoạt khi dữ liệu từ request body không hợp lệ
     * theo các ràng buộc đã định nghĩa (ví dụ: @NotNull, @Size, ...).
     *
     * @param ex      ngoại lệ chứa danh sách các lỗi xác thực
     * @param request yêu cầu HTTP hiện tại
     * @return phản hồi lỗi với mã trạng thái 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Lấy thông điệp lỗi đầu tiên từ danh sách lỗi xác thực
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    /**
     * Xử lý ngoại lệ tham số không hợp lệ (IllegalArgumentException).
     * Xảy ra khi một phương thức nhận được tham số không phù hợp.
     *
     * @param ex      ngoại lệ chứa thông điệp mô tả lỗi
     * @param request yêu cầu HTTP hiện tại
     * @return phản hồi lỗi với mã trạng thái 400 (Bad Request)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Xử lý ngoại lệ trạng thái không hợp lệ (IllegalStateException).
     * Xảy ra khi một thao tác được thực hiện trong trạng thái không phù hợp.
     *
     * @param ex      ngoại lệ chứa thông điệp mô tả lỗi
     * @param request yêu cầu HTTP hiện tại
     * @return phản hồi lỗi với mã trạng thái 400 (Bad Request)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Xử lý tất cả các ngoại lệ chung không được bắt bởi các handler khác.
     * Đây là handler cuối cùng để đảm bảo mọi lỗi đều được xử lý
     * và trả về phản hồi thống nhất cho client.
     *
     * @param ex      ngoại lệ chung
     * @param request yêu cầu HTTP hiện tại
     * @return phản hồi lỗi với mã trạng thái 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request.getRequestURI());
    }

    /**
     * Phương thức tiện ích để xây dựng phản hồi lỗi thống nhất.
     * Tạo đối tượng ErrorResponse với đầy đủ thông tin về lỗi
     * bao gồm: mã trạng thái, thông điệp, loại lỗi, thời gian và đường dẫn.
     *
     * @param status  mã trạng thái HTTP
     * @param message thông điệp mô tả lỗi
     * @param path    đường dẫn API gây ra lỗi
     * @return phản hồi lỗi được bọc trong ResponseEntity
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, String path) {
        ErrorResponse error = ErrorResponse.builder()
                .status(status.value())       // Mã trạng thái HTTP (ví dụ: 400, 500)
                .message(message)             // Thông điệp lỗi chi tiết
                .error(status.getReasonPhrase()) // Tên loại lỗi (ví dụ: "Bad Request")
                .timestamp(LocalDateTime.now())  // Thời điểm xảy ra lỗi
                .path(path)                   // Đường dẫn API gây ra lỗi
                .build();
        return new ResponseEntity<>(error, status);
    }
}
