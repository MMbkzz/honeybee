package com.stackstech.dcp.core.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRedisCache {

    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract String getKey(String key);

    /**
     * 设置过期时间
     *
     * @param key
     * @param time
     */
    public void expire(String key, Long time) {
        stringRedisTemplate.expire(getKey(key), time, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        return stringRedisTemplate.getExpire(getKey(key), TimeUnit.MILLISECONDS);
    }

    public void clear() {
        Set<String> keys = keys();
        if (CollectionUtils.isNotEmpty(keys)) {
            stringRedisTemplate.delete(keys);
        }
    }

    public Set<String> keys() {
        return stringRedisTemplate.keys(getKey("*"));
    }

    /**
     * 根据key模糊查询
     *
     * @param key
     * @return
     */
    public Set<String> keys(String key) {
        return stringRedisTemplate.keys(key + "*");
    }

}
