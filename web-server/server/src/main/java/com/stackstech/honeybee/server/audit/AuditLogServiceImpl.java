package com.stackstech.honeybee.server.audit;

import com.stackstech.honeybee.server.core.entity.AuditLogEntity;
import com.stackstech.honeybee.server.core.mapper.AuditLogMapper;
import com.stackstech.honeybee.server.core.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuditLogServiceImpl implements DataService<AuditLogEntity> {

    @Autowired
    private AuditLogMapper mapper;

    @Override
    public boolean add(AuditLogEntity entity) {
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(AuditLogEntity entity) {
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId) {
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
