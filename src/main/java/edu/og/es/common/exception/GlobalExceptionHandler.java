package edu.og.es.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import edu.og.es.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	  // 1) 유저 못 찾았을 때 (예상 가능한 비즈니스 예외)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException e,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String errorCode = "USER_NOT_FOUND";

        // 예: [Exception] : status=404 path=/users/10 code=USER_NOT_FOUND message=존재하지 않는 사용자입니다.
        log.warn("[Exception] : status={} path={} code={} message={}",
                status.value(), request.getRequestURI(), errorCode, e.getMessage());

        ErrorResponse body = ErrorResponse.of(errorCode, e.getMessage());
        return ResponseEntity.status(status).body(body);
    }

    // 2) 잘못된 요청값일 때 (숫자 범위, 형식 오류 등)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException e,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorCode = "BAD_REQUEST";

        log.warn("[Exception] : status={} path={} code={} message={}",
                status.value(), request.getRequestURI(),  errorCode, e.getMessage() );

        ErrorResponse body = ErrorResponse.of(errorCode, e.getMessage());
        return ResponseEntity.status(status).body(body);
    }

    // 3) 그 외 예상 못 한 예외들
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorCode = "INTERNAL_SERVER_ERROR";

        log.error("[Exception] : status={} path={} code={} message={}",
                status.value(), request.getRequestURI(),errorCode,e.getMessage());
        
        log.error("Unhandled exception stacktrace", e);

        ErrorResponse body = ErrorResponse.of(errorCode, "서버 오류가 발생했습니다.");
        return ResponseEntity.status(status).body(body);
    }
}
