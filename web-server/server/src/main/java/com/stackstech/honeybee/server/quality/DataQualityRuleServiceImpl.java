package com.stackstech.honeybee.server.quality;

import com.stackstech.honeybee.server.core.entity.QualityRuleEntity;
import com.stackstech.honeybee.server.core.service.DataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataQualityRuleServiceImpl implements DataService<QualityRuleEntity> {

    @Override
    public boolean add(QualityRuleEntity entity) {
        return false;
    }

    @Override
    public boolean update(QualityRuleEntity entity) {
        return false;
    }

    @Override
    public boolean delete(Long recordId) {
        return false;
    }

    @Override
    public QualityRuleEntity getSingle(Long recordId) {
        return null;
    }

    @Override
    public QualityRuleEntity getSingle(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public List<QualityRuleEntity> get(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return null;
    }
}
