package com.stackstech.honeybee.server.system.service.impl;

import com.stackstech.honeybee.common.cache.DataCacheHelper;
import com.stackstech.honeybee.server.system.entity.DataCacheEntity;
import com.stackstech.honeybee.server.system.service.DataCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataCacheServiceImpl implements DataCacheService {

    @Autowired
    private DataCacheHelper dataCacheHelper;

    @Override
    public void addDataCache(DataCacheEntity entity) {
        dataCacheHelper.add(entity);
    }

    @Override
    public DataCacheEntity getDataCache(String uuid) {
        return dataCacheHelper.get(uuid);
    }

    @Override
    public boolean delete(String uuid) {
        return dataCacheHelper.delete(uuid);
    }

    @Override
    public List<DataCacheEntity> get(int pageStart, int pageSize) {
        return dataCacheHelper.get(pageStart, pageSize);
    }

    @Override
    public int getTotalCount() {
        return dataCacheHelper.getTotalCount();
    }


}
