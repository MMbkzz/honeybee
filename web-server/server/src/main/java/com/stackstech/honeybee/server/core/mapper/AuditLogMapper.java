package com.stackstech.honeybee.server.core.mapper;

import com.stackstech.honeybee.server.core.entity.AuditLogEntity;

public interface AuditLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AuditLogEntity record);

    int insertSelective(AuditLogEntity record);

    AuditLogEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AuditLogEntity record);

    int updateByPrimaryKey(AuditLogEntity record);
}