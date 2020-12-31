package com.stackstech.honeybee.server.assets;

import com.stackstech.honeybee.server.core.entity.DataRecyclerEntity;
import com.stackstech.honeybee.server.core.mapper.DataRecyclerMapper;
import com.stackstech.honeybee.server.core.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataRecyclerServiceImpl implements DataService<DataRecyclerEntity> {

    @Autowired
    private DataRecyclerMapper mapper;

    @Override
    public boolean add(DataRecyclerEntity entity) {
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(DataRecyclerEntity entity) {
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public DataRecyclerEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public List<DataRecyclerEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }
}