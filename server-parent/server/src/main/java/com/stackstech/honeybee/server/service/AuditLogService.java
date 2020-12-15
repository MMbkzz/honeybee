package com.stackstech.honeybee.server.service;

public interface AuditLogService {

    void logBegin(String taskName);

    void logEnd();

    void logStatus(String statusCode);

    void logMessage(String message);

    void logContext(String context);

    void insertLogs();

}