package com.stackstech.honeybee.server.audit.service;

import com.stackstech.honeybee.server.core.entity.AuditLogEntity;

import java.util.List;
import java.util.Map;

public interface AuditLogService {

    AuditLogEntity getAuditLog(Long id);

    List<AuditLogEntity> getAuditLogs(Map<String, Object> parameter);

    Integer getAuditLogCount(Map<String, Object> parameter);

    Boolean addAuditLog(AuditLogEntity entity);
}
