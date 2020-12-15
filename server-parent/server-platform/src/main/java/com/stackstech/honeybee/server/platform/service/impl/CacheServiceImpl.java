package com.stackstech.honeybee.server.platform.service.impl;

import com.stackstech.honeybee.core.cache.ServiceDataCache;
import com.stackstech.honeybee.core.util.JacksonUtil;
import com.stackstech.honeybee.server.platform.service.CacheService;
import com.stackstech.honeybee.server.platform.vo.CacheVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 缓存Service
 */
@Service
@Transactional
public class CacheServiceImpl implements CacheService {

    @Autowired
    private ServiceDataCache serviceDataCache;

    /**
     * 获取缓存列表
     *
     * @param key
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> queryAll(String key) throws Exception {
        List<CacheVO> queryCaches = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<CacheVO> caches = new ArrayList<>();
        Set<String> keys = serviceDataCache.keys(serviceDataCache.getKey("API"));
        if (keys != null && keys.size() > 0) {
            for (String realKey : keys) {
                String preKey = realKey.substring(realKey.lastIndexOf(":") + 1);
                Long expireTime = serviceDataCache.getExpire(preKey);
                if (expireTime != null && expireTime > 0) {
                    CacheVO cacheVO = new CacheVO();
                    cacheVO.setKey(preKey);
                    cacheVO.setValue("*");
                    cacheVO.setExpireTime(expireTime / 1000);
                    caches.add(cacheVO);
                }
            }
        }
        if (caches != null && caches.size() > 0) {
            for (CacheVO cache : caches) {
                cache.setId(caches.indexOf(cache) + 1);
            }
        }
        //模糊查询Key
        queryCaches = this.queryCacheByKey(key, caches);
        map.put("list", queryCaches);
        map.put("count", queryCaches.size());
        return map;
    }

    /**
     * 根据key查询缓存数据
     *
     * @return
     */
    @Override
    public CacheVO queryByKey(CacheVO cacheVO) throws Exception {
        if (cacheVO != null && cacheVO.getKey() != null) {
            Map<String, Object> values = serviceDataCache.get(cacheVO.getKey());
            if (values != null) {
                String response = JacksonUtil.convertToJsonStrs(values);
                cacheVO.setValue((response.length() > 1000 ? response.substring(0, 1000) : response));
            }
        }
        return cacheVO;
    }

    /**
     * 删除缓存
     *
     * @param keys
     * @return
     * @throws Exception
     */
    @Override
    public void delete(List<String> keys) throws Exception {
        for (String key : keys) {
            if (StringUtils.isNotEmpty(key)) {
                key = key.substring(key.lastIndexOf(":") + 1).trim();
            }
            serviceDataCache.delete(key);
        }
    }

    /**
     * 模糊查询
     * * @param key
     *
     * @param caches
     * @return
     */
    private List<CacheVO> queryCacheByKey(String key, List<CacheVO> caches) {
        if (StringUtils.isNotEmpty(key)) {
            List<CacheVO> queryCaches = new ArrayList<>();
            if (caches != null && caches.size() > 0) {
                for (CacheVO cache : caches) {
                    if (cache.getKey().contains(key)) {
                        queryCaches.add(cache);
                    }
                }
            }
            return queryCaches;
        }
        return caches;
    }
}
