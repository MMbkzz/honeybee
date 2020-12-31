package com.stackstech.honeybee.server.core.handler;


import com.stackstech.honeybee.server.core.annotation.AuditOperation;
import com.stackstech.honeybee.server.core.entity.AuditLogEntity;
import com.stackstech.honeybee.server.core.enums.AuditOperationType;
import com.stackstech.honeybee.server.core.enums.EntityStatusType;
import com.stackstech.honeybee.server.core.service.DataService;
import com.stackstech.honeybee.server.core.utils.CommonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public final class AuditOperationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AuditOperationHandler.class);

    @Autowired
    private DataService<AuditLogEntity> service;
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

            AuditLogEntity log = new AuditLogEntity();
            log.setLogTitle(operation.getDesc());
            log.setLogType(operation.getName());
            log.setLogAudit(type.getName());
            log.setLogContent(builder.toString());
            log.setOwner(1L);
            log.setStatus(EntityStatusType.ENABLE.getStatus());
            log.setCreatetime(new Date());
            log.setUpdatetime(new Date());
            log.setDesc(desc);
            service.add(log);
            LOG.debug("Recording user operation {}", log.toString());
        }
        return result;
    }
}