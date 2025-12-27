package org.example.demomanagementsystemcproject.config;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionLogger {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionLogger.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleJsonParseError(HttpServletRequest req, HttpMessageNotReadableException ex) {
        logger.error("JSON 解析错误 -> 请求: {} {}, 消息: {}, 根因: {}",
                req.getMethod(), req.getRequestURI(),
                ex.getMessage(),
                ex.getRootCause() == null ? "null" : ex.getRootCause().toString());
        // 继续抛出，使用默认异常处理（返回 400）
        throw ex;
    }
}
