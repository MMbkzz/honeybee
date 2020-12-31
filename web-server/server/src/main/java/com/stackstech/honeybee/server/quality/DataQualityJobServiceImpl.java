package com.stackstech.honeybee.server.quality;

import com.stackstech.honeybee.server.core.entity.QualityJobEntity;
import com.stackstech.honeybee.server.core.mapper.QualityJobMapper;
import com.stackstech.honeybee.server.core.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataQualityJobServiceImpl implements DataService<QualityJobEntity> {

    @Autowired
    private QualityJobMapper mapper;

    @Override
    public boolean add(QualityJobEntity entity) {
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(QualityJobEntity entity) {
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public QualityJobEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public QualityJobEntity getSingle(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public List<QualityJobEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }
}
