package com.stackstech.honeybee.server.assets;

import com.stackstech.honeybee.server.core.entity.AssetsCatalogEntity;
import com.stackstech.honeybee.server.core.mapper.AssetsCatalogMapper;
import com.stackstech.honeybee.server.core.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssetsCatalogServiceImpl implements DataService<AssetsCatalogEntity> {

    @Autowired
    private AssetsCatalogMapper mapper;

    @Override
    public boolean add(AssetsCatalogEntity entity) {
        return mapper.insertSelective(entity) > 0;
    }

    @Override
    public boolean update(AssetsCatalogEntity entity) {
        return mapper.updateByPrimaryKeySelective(entity) > 0;
    }

    @Override
    public boolean delete(Long recordId) {
        return mapper.deleteByPrimaryKey(recordId) > 0;
    }

    @Override
    public AssetsCatalogEntity getSingle(Long recordId) {
        return mapper.selectByPrimaryKey(recordId);
    }

    @Override
    public AssetsCatalogEntity getSingle(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public List<AssetsCatalogEntity> get(Map<String, Object> parameter) {
        return mapper.selectByParameter(parameter);
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return mapper.selectTotalCount(parameter);
    }
}
