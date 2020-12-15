package com.stackstech.honeybee.apiserver.service.impl;

import com.stackstech.honeybee.apiserver.conf.ServerConfig;
import com.stackstech.honeybee.apiserver.service.AuditLogService;
import com.stackstech.honeybee.core.enums.TaskStatusEnum;
import com.stackstech.honeybee.core.log.LoggerHealper;
import com.stackstech.honeybee.core.log.ServerLogger;
import com.stackstech.honeybee.server.operations.dao.AuditLogMapper;
import com.stackstech.honeybee.server.operations.model.AuditLog;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Override
    public void logBegin(String taskName) {
        AuditLog auditLog = new AuditLog();
        // 设置任务名称
        auditLog.setTaskName(taskName);
        // 设置实例ip端口
        auditLog.setHost(serverConfig.getHost());
        auditLog.setPort(String.valueOf(serverConfig.getPort()));
        // 设置任务开始时间
        auditLog.setTaskStartTime(new Timestamp(System.currentTimeMillis()));
        // 默认状态 - 成功
        auditLog.setStatusCode(TaskStatusEnum.ok.code);

        LoggerHealper.set(auditLog);
    }

    @Override
    public void logEnd() {
        ServerLogger<AuditLog> serverLogger = LoggerHealper.getLocalLog();
        serverLogger.getMessage().setTaskEndTime(new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public void logStatus(String statusCode) {
        ServerLogger<AuditLog> serverLogger = LoggerHealper.getLocalLog();
        serverLogger.getMessage().setStatusCode(statusCode);
    }

    @Override
    public void logMessage(String message) {
        ServerLogger<AuditLog> serverLogger = LoggerHealper.getLocalLog();

        message = (StringUtils.isBlank(serverLogger.getMessage().getMessage()) ? "" : serverLogger.getMessage().getMessage()) + message + ";";

        serverLogger.getMessage().setMessage(message);
    }

    @Override
    public void logContext(String context) {
        ServerLogger<AuditLog> serverLogger = LoggerHealper.getLocalLog();
        serverLogger.getMessage().setContext(context);
    }

    @Override
    public void insertLogs() {
        ServerLogger<AuditLog> serverLogger = LoggerHealper.getLocalLog();
        if (null == serverLogger || null == serverLogger.getMessage()) {
            return;
        }

        AuditLog log = serverLogger.getMessage();
        log.setThread(serverLogger.getThread());
        auditLogMapper.insert(log);

        LoggerHealper.clearLog();
    }

}
