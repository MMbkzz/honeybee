package com.stackstech.honeybee.core.cache;

import org.apache.commons.lang3.StringUtils;

/**
 * redis set操作
 *
 * @param <VT>
 */
public abstract class AbstractRedisStringCache<VT> extends AbstractRedisCache {

    /**
     * 缓存 - 存放
     *
     * @param key   节点
     * @param value 缓存值
     */
    public void add(String key, VT value) {
        stringRedisTemplate.opsForValue().set(getKey(key), output(value));
    }

    /**
     * 缓存 - 获取缓存信息
     *
     * @param key 缓存主键
     */
    public VT get(String key) {
        return input(stringRedisTemplate.opsForValue().get(getKey(key)));
    }


    /**
     * 缓存 - 删除缓存/缓存明细
     *
     * @param key 缓存主键
     */
    public void delete(String key) {
        if (StringUtils.isNotEmpty(key)) {
            stringRedisTemplate.delete(getKey(key));
        }
    }


    public abstract String output(VT value);

    public abstract VT input(String value);


}
