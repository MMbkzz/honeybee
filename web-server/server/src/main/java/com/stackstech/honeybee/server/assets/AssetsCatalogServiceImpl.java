package com.stackstech.honeybee.server.assets;

import com.stackstech.honeybee.server.core.entity.AssetsCatalogEntity;
import com.stackstech.honeybee.server.core.service.DataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssetsCatalogServiceImpl implements DataService<AssetsCatalogEntity> {

    @Override
    public boolean add(AssetsCatalogEntity entity) {
        return false;
    }

    @Override
    public boolean update(AssetsCatalogEntity entity) {
        return false;
    }

    @Override
    public boolean delete(Long recordId) {
        return false;
    }

    @Override
    public AssetsCatalogEntity getSingle(Long recordId) {
        return null;
    }

    @Override
    public AssetsCatalogEntity getSingle(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public List<AssetsCatalogEntity> get(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return null;
    }
}
