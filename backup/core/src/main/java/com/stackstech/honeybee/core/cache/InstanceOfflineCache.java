package com.stackstech.honeybee.core.cache;

import com.stackstech.honeybee.core.constants.CacheKeyConstant;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 应用实例 - 下线中缓存
 */
@Deprecated
@Component
public class InstanceOfflineCache extends AbstractRedisSetCache<String> {

    @Override
    public String getKey(String key) {
        return CacheKeyConstant.getOffline();
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
