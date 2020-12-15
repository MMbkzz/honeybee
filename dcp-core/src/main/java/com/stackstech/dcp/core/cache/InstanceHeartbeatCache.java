package com.stackstech.dcp.core.cache;

import com.google.common.collect.Maps;
import com.stackstech.dcp.core.constants.CacheKeyConstant;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 应用实例心跳包缓存操作
 */
@Component
public class InstanceHeartbeatCache extends AbstractRedisHashCache<Long> {

    @Override
    public String getKey(String key) {
        return CacheKeyConstant.getHeartBeat();
    }

    @Override
    public String output(Long hashValue) {
        return String.valueOf(hashValue);
    }

    @Override
    public Map<String, String> output(Map<String, Long> value) {
        return Maps.transformEntries(value, (s, value1) -> String.valueOf(value1));
    }

    @Override
    public Long input(Object hashValue) {
        return null == hashValue ? new Long(0) : Long.valueOf(hashValue.toString());
    }

    @Override
    public Map<String, Long> input(Map<Object, Object> value) {
        Map<String, Long> result = Maps.newHashMap();
        if (MapUtils.isEmpty(value)) {
            return result;
        }

        value.forEach((key, value1) -> result.put(String.valueOf(key), Long.valueOf(value1.toString())));

        return result;
    }

}