package com.stackstech.honeybee.server.apiserver;

import com.stackstech.honeybee.server.core.entity.DataServiceEntity;
import com.stackstech.honeybee.server.core.mapper.DataServiceMapper;
import com.stackstech.honeybee.server.core.service.DataService;
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
    public boolean add(DataServiceEntity entity) {
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(DataServiceEntity entity) {
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public DataServiceEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public DataServiceEntity getSingle(Map<String, Object> parameter) {
        return null;
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