package com.stackstech.honeybee.server.service.impl;

import com.stackstech.honeybee.server.core.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.mapper.DataServiceMapper;
import com.stackstech.honeybee.server.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * DataServiceImpl
 *
 * @author william
 */
@Service
public class DataServiceImpl implements DataService<DataServiceEntity> {

    @Autowired
    private DataServiceMapper mapper;

    @Override
    public boolean add(DataServiceEntity entity, Long ownerId) {
        entity.create(ownerId);
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(DataServiceEntity entity, Long ownerId) {
        entity.update(ownerId);
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return mapper.deleteByPrimaryKey(recordId, ownerId) > 0;
    }

    @Override
    public DataServiceEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<DataServiceEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }

}
