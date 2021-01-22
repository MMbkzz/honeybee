package com.stackstech.honeybee.server.service.impl;

import com.stackstech.honeybee.server.core.entity.QualityRuleEntity;
import com.stackstech.honeybee.server.dao.QualityRuleMapper;
import com.stackstech.honeybee.server.service.DataService;
import com.stackstech.honeybee.server.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataQualityRuleServiceImpl implements DataService<QualityRuleEntity> {

    @Autowired
    private QualityRuleMapper mapper;

    @Override
    public boolean add(QualityRuleEntity entity, Long ownerId) {
        entity.create(ownerId);
        entity.setRuleCode(CommonUtil.generateEntityCode());
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(QualityRuleEntity entity, Long ownerId) {
        entity.update(ownerId);
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public QualityRuleEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<QualityRuleEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }
}
