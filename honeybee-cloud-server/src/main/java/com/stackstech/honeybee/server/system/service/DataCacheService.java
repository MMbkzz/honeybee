package com.stackstech.honeybee.server.system.service;

import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import com.stackstech.honeybee.server.system.entity.DataCacheEntity;

import java.util.List;

public interface DataCacheService {

    void addDataCache(DataCacheEntity entity) throws ServerException;

    DataCacheEntity getDataCache(String uuid) throws ServerException, DataNotFoundException;

    boolean delete(String uuid) throws ServerException;

    List<DataCacheEntity> get(String keywords, int pageStart, int pageSize) throws ServerException, DataNotFoundException;

    int getTotalCount(String keywords) throws ServerException;
}
