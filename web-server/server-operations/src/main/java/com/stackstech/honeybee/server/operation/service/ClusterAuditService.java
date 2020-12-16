package com.stackstech.honeybee.server.operation.service;

import com.stackstech.honeybee.server.operation.vo.ClusterAuditQueryVO;
import com.stackstech.honeybee.server.operations.model.AuditLog;

import java.util.List;

public interface ClusterAuditService {

    int add(AuditLog auditLog) throws Exception;

    List<AuditLog> queryAll(ClusterAuditQueryVO auditQueryVO) throws Exception;

    int countAll(ClusterAuditQueryVO auditQueryVO) throws Exception;

    AuditLog query(Integer id) throws Exception;
}
