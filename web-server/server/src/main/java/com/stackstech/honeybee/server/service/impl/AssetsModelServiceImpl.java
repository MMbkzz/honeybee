package com.stackstech.honeybee.server.service.impl;

import com.stackstech.honeybee.server.core.entity.AssetsModelEntity;
import com.stackstech.honeybee.server.dao.AssetsModelMapper;
import com.stackstech.honeybee.server.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssetsModelServiceImpl implements DataService<AssetsModelEntity> {

    @Autowired
    private AssetsModelMapper mapper;

    @Override
    public boolean add(AssetsModelEntity entity, Long ownerId) {
        entity.create(ownerId);
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(AssetsModelEntity entity, Long ownerId) {
        entity.update(ownerId);
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public AssetsModelEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
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
