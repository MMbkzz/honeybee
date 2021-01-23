package com.stackstech.honeybee.server.service.impl;

import com.stackstech.honeybee.server.core.entity.AuditLogEntity;
import com.stackstech.honeybee.server.dao.AuditLogMapper;
import com.stackstech.honeybee.server.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

//TODO
@Service
public class AuditLogServiceImpl implements DataService<AuditLogEntity, AuditLogEntity> {

    @Autowired
    private AuditLogMapper mapper;

    @Override
    public boolean add(AuditLogEntity entity, Long ownerId) {
        entity.build(ownerId);
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(AuditLogEntity entity, Long ownerId) {
        entity.update(ownerId);
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public AuditLogEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<AuditLogEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }

}
