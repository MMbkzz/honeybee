package com.stackstech.dcp.apiserver.service.impl;

import com.stackstech.dcp.apiserver.service.CachService;
import com.stackstech.dcp.core.cache.ApiLogDataCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 操作redis缓存服务实现类
 */
@Service
public class CachServiceImpl implements CachService {
    private final Logger log = LoggerFactory.getLogger(CachServiceImpl.class);

    @Autowired
    private ApiLogDataCache apiDataCache;

    @Override
    public void put(String dataServiceId, String resultValue) {
        apiDataCache.put(dataServiceId, dataServiceId, resultValue);
    }

    @Override
    public Object get(String dataServiceId) {
        Map<String, String> StringMap = apiDataCache.get(dataServiceId);
        return StringMap;
    }

    @Override
    public void delete(String dataServiceId) {
        apiDataCache.delete(dataServiceId);
    }
}
