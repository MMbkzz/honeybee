package com.stackstech.honeybee.server.system.service.impl;

import com.stackstech.honeybee.common.cache.DataCacheHelper;
import com.stackstech.honeybee.common.utils.CommonUtil;
import com.stackstech.honeybee.server.core.exception.DataNotFoundException;
import com.stackstech.honeybee.server.core.exception.ServerException;
import com.stackstech.honeybee.server.system.entity.DataCacheEntity;
import com.stackstech.honeybee.server.system.service.DataCacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataCacheServiceImpl implements DataCacheService {

    @Autowired
    private DataCacheHelper dataCacheHelper;

    @Override
    public void addDataCache(DataCacheEntity entity) throws ServerException {
        dataCacheHelper.add(entity);
    }

    @Override
    public DataCacheEntity getDataCache(String uuid) throws ServerException, DataNotFoundException {
        return dataCacheHelper.get(uuid);
    }

    @Override
    public boolean delete(String uuid) throws ServerException {
        return dataCacheHelper.delete(uuid);
    }

    @Override
    public List<DataCacheEntity> get(String keywords, int pageStart, int pageSize) throws ServerException, DataNotFoundException {
        List<DataCacheEntity> entities;
        if (StringUtils.isNotEmpty(keywords)) {
            entities = dataCacheHelper.get(keywords, pageStart, pageSize);
        } else {
            entities = dataCacheHelper.get(pageStart, pageSize);
        }
        CommonUtil.isEmpty(entities);
        return entities;
    }

    @Override
    public int getTotalCount(String keywords) throws ServerException {
        if (StringUtils.isNotEmpty(keywords)) {
            return dataCacheHelper.getTotalCount(keywords);
        }
        return dataCacheHelper.getTotalCount();
    }


}
