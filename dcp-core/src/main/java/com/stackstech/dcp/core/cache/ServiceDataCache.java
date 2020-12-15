package com.stackstech.dcp.core.cache;

import com.google.common.collect.Maps;
import com.stackstech.dcp.core.constants.CacheKeyConstant;
import com.stackstech.dcp.core.util.JacksonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 服务请求数据缓存
 */
@Component
public class ServiceDataCache extends AbstractRedisHashCache<Object> {

    @Override

    public String getKey(String key) {
        return CacheKeyConstant.getDataServiceKey(key);
    }

    @Override
    public String output(Object hashValue) {
        String value = StringUtils.EMPTY;
        try {
            value = objectMapper.writeValueAsString(hashValue);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return value;
    }

    @Override
    public Map<String, String> output(Map<String, Object> value) {
        return Maps.transformEntries(value, (s, value1) -> output(value1));
    }

    @Override
    public Object input(Object hashValue) {
        return JacksonUtil.jsonToBean((String) hashValue, Object.class);
    }

    @Override
    public Map<String, Object> input(Map<Object, Object> value) {
        Map<String, Object> result = Maps.newHashMap();
        if (MapUtils.isEmpty(value)) {
            return result;
        }

        value.forEach((key, value1) -> result.put(String.valueOf(key), input(value1)));

        return result;
    }
}
