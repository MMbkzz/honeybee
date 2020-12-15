package com.stackstech.dcp.server.platform.service;

import com.stackstech.dcp.server.platform.vo.CacheVO;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface CacheService {

    Map<String, Object> queryAll(String key) throws Exception;

    CacheVO queryByKey(CacheVO cacheVO) throws Exception;

    void delete(List<String> keys) throws Exception;
}
