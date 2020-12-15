package com.stackstech.honeybee.core.cache;

import com.google.common.collect.Maps;
import com.stackstech.honeybee.core.constants.CacheKeyConstant;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 应用实例预期资源缓存操作
 */
@Component
public class ResourceExpectCache extends AbstractRedisHashCache<Integer> {

    @Override
    public String getKey(String key) {
        return CacheKeyConstant.getResourceExpect(key);
    }

    @Override
    public String output(Integer hashValue) {
        return String.valueOf(hashValue);
    }

    @Override
    public Map<String, String> output(Map<String, Integer> value) {
        return Maps.transformEntries(value, (s, value1) -> String.valueOf(value1));
    }

    @Override
    public Integer input(Object hashValue) {
        return null == hashValue ? null : Integer.valueOf(hashValue.toString());
    }

    @Override
    public Map<String, Integer> input(Map<Object, Object> value) {
        Map<String, Integer> result = Maps.newHashMap();
        if (MapUtils.isEmpty(value)) {
            return result;
        }

        value.forEach((key, value1) -> result.put(String.valueOf(key), Integer.valueOf(value1.toString())));

        return result;
    }
}