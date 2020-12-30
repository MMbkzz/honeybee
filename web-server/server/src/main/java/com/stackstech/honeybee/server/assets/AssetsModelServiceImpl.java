package com.stackstech.honeybee.server.assets;

import com.stackstech.honeybee.server.core.entity.AssetsModelEntity;
import com.stackstech.honeybee.server.core.service.DataService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssetsModelServiceImpl implements DataService<AssetsModelEntity> {

    @Override
    public boolean add(AssetsModelEntity entity) {
        return false;
    }

    @Override
    public boolean update(AssetsModelEntity entity) {
        return false;
    }

    @Override
    public boolean delete(Long recordId) {
        return false;
    }

    @Override
    public AssetsModelEntity getSingle(Long recordId) {
        return null;
    }

    @Override
    public AssetsModelEntity getSingle(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public List<AssetsModelEntity> get(Map<String, Object> parameter) {
        return null;
    }

    @Override
    public Integer getTotalCount(Map<String, Object> parameter) {
        return null;
    }
}
