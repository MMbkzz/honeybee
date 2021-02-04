package com.stackstech.honeybee.server.assets.service.impl;

import com.stackstech.honeybee.common.entity.JsonParameterMap;
import com.stackstech.honeybee.server.assets.dao.AssetsModelMapper;
import com.stackstech.honeybee.server.assets.entity.AssetsModelEntity;
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
        //TODO
        JsonParameterMap meta = new JsonParameterMap();
        meta.put("testmeta1", "testvalue");
        meta.put("testmeta2", true);
        entity.setDatasourceMeta(meta);
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(AssetsModelEntity entity) {
        //TODO
        JsonParameterMap meta = new JsonParameterMap();
        meta.put("testmeta1", "testvalue");
        meta.put("testmeta2", true);
        entity.setDatasourceMeta(meta);
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId, Long ownerId) {
        //TODO insert to data recycler
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
