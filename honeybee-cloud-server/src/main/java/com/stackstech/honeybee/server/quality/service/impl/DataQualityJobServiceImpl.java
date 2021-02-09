package com.stackstech.honeybee.server.quality.service.impl;

import com.stackstech.honeybee.server.core.service.BaseDataService;
import com.stackstech.honeybee.server.quality.dao.QualityJobMapper;
import com.stackstech.honeybee.server.quality.entity.QualityJobEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataQualityJobServiceImpl implements BaseDataService<QualityJobEntity> {

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
    public boolean delete(Long recordId, Long ownerId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public QualityJobEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
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
