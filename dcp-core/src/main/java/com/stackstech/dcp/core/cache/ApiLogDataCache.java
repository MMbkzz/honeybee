package com.stackstech.dcp.core.cache;

import com.google.common.collect.Maps;
import com.stackstech.dcp.core.constants.CacheKeyConstant;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 请求API数据缓存
 */
@Component
public class ApiLogDataCache extends AbstractRedisHashCache<String> {
    @Override

    public String getKey(String key) {
        return CacheKeyConstant.getLogServiceKey(key);
    }

    @Override
    public String output(String hashValue) {
        return String.valueOf(hashValue);
    }

    @Override
    public Map<String, String> output(Map<String, String> value) {
        return Maps.transformEntries(value, (s, value1) -> String.valueOf(value1));
    }

    @Override
    public String input(Object hashValue) {
        return null == hashValue ? StringUtils.EMPTY : String.valueOf(hashValue.toString());
    }

    @Override
    public Map<String, String> input(Map<Object, Object> value) {
        Map<String, String> result = Maps.newHashMap();
        if (MapUtils.isEmpty(value)) {
            return result;
        }
        value.forEach((key, value1) -> result.put(String.valueOf(key), String.valueOf(value1.toString())));
        return result;
    }
}
