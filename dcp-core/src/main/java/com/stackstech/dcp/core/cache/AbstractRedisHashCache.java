package com.stackstech.dcp.core.cache;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * redis hash操作
 *
 * @param <VT>
 */
public abstract class AbstractRedisHashCache<VT> extends AbstractRedisCache {

    /**
     * 缓存 - 存放
     *
     * @param key       节点
     * @param hashKey   缓存信息
     * @param hashValue 缓存值
     */
    public void put(String key, String hashKey, VT hashValue) {
        stringRedisTemplate.opsForHash().put(getKey(key), hashKey, output(hashValue));
    }

    /**
     * 缓存 - 存放
     * 原生key值操作
     *
     * @param key       节点
     * @param hashKey   缓存信息
     * @param hashValue 缓存值
     */
    public void putNaturo(String key, String hashKey, VT hashValue) {
        stringRedisTemplate.opsForHash().put(key, hashKey, output(hashValue));
    }

    /**
     * 缓存 - 存放所有
     *
     * @param key    节点
     * @param params 缓存信息
     */
    public void put(String key, Map<String, VT> params) {
        stringRedisTemplate.opsForHash().putAll(getKey(key), output(params));
    }

    /**
     * 缓存 - 存放
     *
     * @param key   dataServiceId
     * @param value 缓存信息
     */
    public void put(String key, String value) {
        stringRedisTemplate.opsForValue().set(getKey(key), value);
    }

    /**
     * 缓存 - 获取缓存信息
     *
     * @param key 缓存主键
     */
    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(getKey(key));
    }

    /**
     * 缓存 - 获取缓存信息
     *
     * @param key 缓存主键
     */
    public Map<String, VT> get(String key) {
        return input(stringRedisTemplate.opsForHash().entries(getKey(key)));
    }

    /**
     * 缓存 - 获取缓存信息
     *
     * @param key 缓存主键
     */
    public Map<String, VT> getNaturo(String key) {
        return input(stringRedisTemplate.opsForHash().entries(key));
    }

    /**
     * 缓存 - 获取缓存信息明细
     *
     * @param key     缓存主键
     * @param hashKey hash-key
     */
    public VT get(String key, String hashKey) {
        return input(stringRedisTemplate.opsForHash().get(getKey(key), hashKey));
    }

    /**
     * 缓存 - 获取缓存信息明细
     * 原生key值操作
     *
     * @param key     缓存主键
     * @param hashKey hash-key
     */
    public VT getNaturo(String key, String hashKey) {
        return input(stringRedisTemplate.opsForHash().get(key, hashKey));
    }

    public Long size(String key) {
        return stringRedisTemplate.opsForHash().size(getKey(key));
    }

    /**
     * 缓存 - 删除缓存/缓存明细
     *
     * @param key     缓存主键
     * @param hashKey hash-key
     */
    public void delete(String key, String... hashKey) {
        if (StringUtils.isBlank(key)) {
            Set<String> keySet = stringRedisTemplate.keys(getKey("*"));
            if (CollectionUtils.isNotEmpty(keySet)) {
                for (String keys : keySet) {
                    stringRedisTemplate.opsForHash().delete(keys, hashKey);
                }
            }
        } else {
            if (null == hashKey || 0 == hashKey.length) {
                stringRedisTemplate.delete(getKey(key));
            } else {
                stringRedisTemplate.opsForHash().delete(getKey(key), hashKey);
            }
        }
    }

    /**
     * 缓存 - 删除缓存/缓存明细
     *
     * @param key     缓存主键
     * @param hashKey hash-key
     */
    public void deleteNaturo(String key, String... hashKey) {
        if (null == hashKey || 0 == hashKey.length) {
            stringRedisTemplate.delete(key);
        } else {
            stringRedisTemplate.opsForHash().delete(key, hashKey);
        }
    }

    public abstract String output(VT hashValue);

    public abstract Map<String, String> output(Map<String, VT> value);

    public abstract VT input(Object hashValue);

    public abstract Map<String, VT> input(Map<Object, Object> value);

}
