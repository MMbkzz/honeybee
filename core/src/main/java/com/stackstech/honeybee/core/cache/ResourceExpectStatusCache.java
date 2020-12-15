package com.stackstech.honeybee.core.cache;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.core.constants.CacheKeyConstant;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 应用实例预期资源执行状态缓存操作
 */
@Component
public class ResourceExpectStatusCache extends AbstractRedisHashCache<String> {

    @Override
    public String getKey(String key) {
        return CacheKeyConstant.getResourceExpectStatus(key);
    }

    @Override
    public String output(String hashValue) {
        return hashValue;
    }

    @Override
    public Map<String, String> output(Map<String, String> value) {
        return value;
    }

    @Override
    public String input(Object hashValue) {
        return String.valueOf(hashValue);
    }

    @Override
    public Map<String, String> input(Map<Object, Object> value) {
        Map<String, String> result = Maps.newHashMap();
        if (MapUtils.isEmpty(value)) {
            return result;
        }

        value.forEach((key, value1) -> result.put(String.valueOf(key), String.valueOf(value1)));

        return result;
    }
}