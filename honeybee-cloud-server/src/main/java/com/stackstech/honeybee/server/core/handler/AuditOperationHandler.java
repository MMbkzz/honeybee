package com.stackstech.honeybee.server.core.handler;


import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.audit.entity.AuditLogEntity;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import com.stackstech.honeybee.server.core.service.DataService;
import com.stackstech.honeybee.server.core.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Optional;

/**
 * Audit operation handler
 *
 * @author William
 * @since 1.0
 */
@Aspect
@Component
@Slf4j
public final class AuditOperationHandler {


    @Autowired
    private DataService<AuditLogEntity, AuditLogEntity> service;
    @Autowired
    private HttpServletRequest request;

    /**
     * 当用户操作完成后，记录用户的操作详细日志
     *
     * @param point Proceeding join point
     * @return Object
     * @throws Throwable Exceptions
     */
    @Around("@annotation(com.stackstech.honeybee.server.core.annotation.AuditOperation)")
    public Object aroundAdvice(ProceedingJoinPoint point) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();

        AuditOperation auditOperation = method.getAnnotation(AuditOperation.class);
        AuditOperationType type = auditOperation.type();
        AuditOperationType operation = auditOperation.operation();
        String desc = auditOperation.desc();
        String stacktrace = null;

        Object result = null;
        try {
            result = point.proceed();
        } catch (Exception e) {
            operation = AuditOperationType.ERROR;
            stacktrace = CommonUtil.getStackTrace(e);
            throw e;
        } finally {
            String clientIp = CommonUtil.getRequestIpAddr(request);
            String uri = request.getRequestURI();
            //String[] clientinfo = CommonUtil.getClientInfo(request);
            String requestHeaders = CommonUtil.getRequestHeaders(request);

            StringBuilder builder = new StringBuilder();
            builder.append("URI: ").append(uri).append("\n");
            builder.append("IP: ").append(clientIp).append("\n");
            builder.append("REQUEST: ").append(requestHeaders).append("\n");
            String finalStacktrace = stacktrace;
            Optional.ofNullable(stacktrace).ifPresent(u -> {
                builder.append("ERROR: ").append(finalStacktrace).append("\n");
            });

            AuditLogEntity auditLog = new AuditLogEntity();
            auditLog.setLogTitle(operation.getDesc());
            auditLog.setLogType(operation.getName());
            auditLog.setLogAudit(type.getName());
            auditLog.setLogContent(builder.toString());
            auditLog.setOwner(1L);
            auditLog.setStatus(EntityStatusType.ENABLE.getStatus());
            auditLog.setCreatetime(new Date());
            auditLog.setUpdatetime(new Date());
            auditLog.setDesc(desc);
            service.add(auditLog, 1L);
            log.debug("Recording user operation {}", log.toString());
        }
        return result;
    }
}