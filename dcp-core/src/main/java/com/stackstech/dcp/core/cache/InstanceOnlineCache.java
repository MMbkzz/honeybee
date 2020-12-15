package com.stackstech.dcp.core.cache;

import com.stackstech.dcp.core.constants.CacheKeyConstant;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 应用实例 - 上线中缓存
 */
@Deprecated
@Component
public class InstanceOnlineCache extends AbstractRedisSetCache<String> {

    @Override
    public String getKey(String key) {
        return CacheKeyConstant.getOnline();
    }

    @Override
    public String outputOne(String value) {
        return value;
    }

    @Override
    public String[] output(String... value) {
        return value;
    }

    @Override
    public String input(String value) {
        return value;
    }

    @Override
    public Set<String> input(Set<String> value) {
        return value;
    }

}
