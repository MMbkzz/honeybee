package com.stackstech.dcp.server.operation.service;

import com.stackstech.dcp.server.operation.vo.SysAuditQueryVO;
import com.stackstech.dcp.server.operations.model.SysAuditLog;

import java.util.Map;

public interface SysAuditLogService {

    int add(SysAuditLog sysAuditLog) throws Exception;

    Map<String, Object> queryAll(SysAuditQueryVO sysAuditQueryVO) throws Exception;

    SysAuditLog query(Long id) throws Exception;

    int countAll(SysAuditQueryVO sysAuditQueryVO) throws Exception;
}
