package com.stackstech.dcp.core.cache;

import java.util.Set;

/**
 * redis set操作
 *
 * @param <VT>
 */
public abstract class AbstractRedisSetCache<VT> extends AbstractRedisCache {

    /**
     * 缓存 - 存放
     *
     * @param key   节点
     * @param value 缓存值
     */
    public void add(String key, VT... value) {
        stringRedisTemplate.opsForSet().add(getKey(key), output(value));
    }

    /**
     * 缓存 - 获取缓存信息
     *
     * @param key 缓存主键
     */
    public Set<VT> get(String key) {
        return input(stringRedisTemplate.opsForSet().members(getKey(key)));
    }

    /**
     * 缓存 - 获取缓存信息
     *
     * @param key 缓存主键
     */
    public VT pop(String key) {
        return input(stringRedisTemplate.opsForSet().pop(getKey(key)));
    }

    /**
     * 判断 - 是否存在
     *
     * @param key   缓存主键
     * @param value 缓存值
     * @return
     */
    public boolean isMember(String key, VT value) {
        return stringRedisTemplate.opsForSet().isMember(getKey(key), outputOne(value));
    }

    public Long size(String key) {
        return stringRedisTemplate.opsForSet().size(getKey(key));
    }

    /**
     * 缓存 - 删除缓存/缓存明细
     *
     * @param key   缓存主键
     * @param value 缓存值
     */
    public void delete(String key, VT... value) {
        if (null == value || 0 == value.length) {
            stringRedisTemplate.delete(getKey(key));
        } else {
            stringRedisTemplate.opsForSet().remove(getKey(key), value);
        }
    }

    public abstract String outputOne(VT value);

    public abstract String[] output(VT... value);

    public abstract VT input(String value);

    public abstract Set<VT> input(Set<String> value);


}
