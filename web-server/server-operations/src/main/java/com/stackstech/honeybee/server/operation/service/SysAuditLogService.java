package com.stackstech.honeybee.server.operation.service;

import com.stackstech.honeybee.server.operation.vo.SysAuditQueryVO;
import com.stackstech.honeybee.server.operations.model.SysAuditLog;

import java.util.Map;

public interface SysAuditLogService {

    int add(SysAuditLog sysAuditLog) throws Exception;

    Map<String, Object> queryAll(SysAuditQueryVO sysAuditQueryVO) throws Exception;

    SysAuditLog query(Long id) throws Exception;

    int countAll(SysAuditQueryVO sysAuditQueryVO) throws Exception;
}
