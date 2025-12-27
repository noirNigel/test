package org.example.demomanagementsystemcproject.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.demomanagementsystemcproject.entity.OperationLogEntity;
import org.example.demomanagementsystemcproject.service.OperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class OperationLogAspect {

    private static final Logger log = LoggerFactory.getLogger(OperationLogAspect.class);

    private final OperationLogService operationLogService;
    private final HttpServletRequest request;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OperationLogAspect(OperationLogService operationLogService,
                              HttpServletRequest request) {
        this.operationLogService = operationLogService;
        this.request = request;
    }

    @Around("@annotation(org.example.demomanagementsystemcproject.system.OperationLog)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        Object result = null;
        Exception ex = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            ex = e;
            throw e;
        } finally {
            long cost = System.currentTimeMillis() - start;
            saveLog(joinPoint, cost, ex);
        }
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long cost, Exception ex) {
        try {
            OperationLogEntity logEntity = new OperationLogEntity();

            // 从 JwtFilter 里保存的 request attribute 中取用户名（如果没取到就 unknown）
            String username = (String) request.getAttribute("username");
            logEntity.setUsername(username != null ? username : "unknown");

            // 模块 & 方法
            logEntity.setModule(joinPoint.getTarget().getClass().getSimpleName());
            logEntity.setMethod(joinPoint.getSignature().getName());

            // 请求相关信息
            logEntity.setAction(request.getRequestURI());
            logEntity.setRequestUri(request.getRequestURI());
            logEntity.setRequestMethod(request.getMethod());
            logEntity.setIp(request.getRemoteAddr());
            logEntity.setCostTime(cost);

            // 请求参数
            try {
                logEntity.setRequestParams(objectMapper.writeValueAsString(request.getParameterMap()));
            } catch (Exception ignored) {
                logEntity.setRequestParams("{}");
            }

            // 关键：给 created_at 赋值，避免你现在的报错
            logEntity.setCreatedAt(LocalDateTime.now());

            // TODO: 如果你有异常字段（比如 errorMsg），这里可以顺便记录
            // if (ex != null) {
            //     logEntity.setErrorMsg(ex.getMessage());
            // }

            operationLogService.save(logEntity);

        } catch (Exception e) {
            log.error("保存操作日志失败：{}", e.getMessage());
        }
    }
}
