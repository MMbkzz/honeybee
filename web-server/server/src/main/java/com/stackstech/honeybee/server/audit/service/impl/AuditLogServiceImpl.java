package com.stackstech.honeybee.server.audit.service.impl;

import com.stackstech.honeybee.server.audit.service.AuditLogService;
import com.stackstech.honeybee.server.core.entity.AuditLogEntity;
import com.stackstech.honeybee.server.core.mapper.AuditLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogMapper mapper;

    @Override
    public AuditLogEntity getAuditLog(Long id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<AuditLogEntity> getAuditLogs(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getAuditLogCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }

    @Override
    public Boolean addAuditLog(AuditLogEntity entity) {
        return mapper.insertSelective(entity) > 0;
    }
}
