package com.stackstech.honeybee.server.assets;

import com.stackstech.honeybee.server.core.entity.AssetsModelEntity;
import com.stackstech.honeybee.server.core.mapper.AssetsModelMapper;
import com.stackstech.honeybee.server.core.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssetsModelServiceImpl implements DataService<AssetsModelEntity> {

    @Autowired
    private AssetsModelMapper mapper;

    @Override
    public boolean add(AssetsModelEntity entity) {
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(AssetsModelEntity entity) {
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public AssetsModelEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public AssetsModelEntity getSingle(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public List<AssetsModelEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }
}
