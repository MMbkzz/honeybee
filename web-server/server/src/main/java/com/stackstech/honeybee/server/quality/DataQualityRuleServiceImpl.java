package com.stackstech.honeybee.server.quality;

import com.stackstech.honeybee.server.core.entity.QualityRuleEntity;
import com.stackstech.honeybee.server.core.mapper.QualityRuleMapper;
import com.stackstech.honeybee.server.core.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataQualityRuleServiceImpl implements DataService<QualityRuleEntity> {

    @Autowired
    private QualityRuleMapper mapper;

    @Override
    public boolean add(QualityRuleEntity entity) {
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(QualityRuleEntity entity) {
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public QualityRuleEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public QualityRuleEntity getSingle(Map<String, Object> parameter) {
        return null;
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
