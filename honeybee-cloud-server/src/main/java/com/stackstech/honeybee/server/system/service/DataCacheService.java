package com.stackstech.honeybee.server.system.service;

import com.stackstech.honeybee.server.system.entity.DataCacheEntity;

import java.util.List;

public interface DataCacheService {

    void addDataCache(DataCacheEntity entity);

    DataCacheEntity getDataCache(String uuid);

    boolean delete(String uuid);

    List<DataCacheEntity> get(int pageStart, int pageSize);

    int getTotalCount();
}
