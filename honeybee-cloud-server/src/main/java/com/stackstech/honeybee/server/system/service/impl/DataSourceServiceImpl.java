package com.stackstech.honeybee.server.system.service.impl;

import com.stackstech.honeybee.server.core.service.DataService;
import com.stackstech.honeybee.server.system.dao.DataSourceMapper;
import com.stackstech.honeybee.server.system.entity.DataSourceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataSourceServiceImpl implements DataService<DataSourceEntity> {

    @Autowired
    private DataSourceMapper mapper;

    @Override
    public boolean add(DataSourceEntity entity) {
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(DataSourceEntity entity) {
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public DataSourceEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<DataSourceEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }

}
