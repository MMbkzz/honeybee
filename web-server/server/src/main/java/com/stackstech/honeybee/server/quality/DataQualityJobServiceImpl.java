package com.stackstech.honeybee.server.quality;

import com.stackstech.honeybee.server.core.entity.QualityJobEntity;
import com.stackstech.honeybee.server.core.service.DataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataQualityJobServiceImpl implements DataService<QualityJobEntity> {

    @Override
    public boolean add(QualityJobEntity entity) {
        return false;
    }

    @Override
    public boolean update(QualityJobEntity entity) {
        return false;
    }

    @Override
    public boolean delete(Long recordId) {
        return false;
    }

    @Override
    public QualityJobEntity getSingle(Long recordId) {
        return null;
    }

    @Override
    public QualityJobEntity getSingle(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public List<QualityJobEntity> get(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return null;
    }
}
