package com.stackstech.honeybee.server.common.utils;


import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.stackstech.honeybee.server.core.enums.CacheKey;
import com.stackstech.honeybee.server.core.enums.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.util.SafeEncoder;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;

/**
 * Redis Cache utils
 *
 * @author William
 * @since 1.0
 */
@Slf4j
@Component
public final class CacheUtil {

    private static final String LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    public static Joiner joiner = Joiner.on(Constant.SEPARATOR).skipNulls();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

//    protected void set(String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
//    }
//
//    protected void set(String key, String value, long timeout, TimeUnit unit) {
//        redisTemplate.opsForValue().set(key, value, timeout, unit);
//    }
//
//    protected String get(String key) {
//        return redisTemplate.opsForValue().get(key);
//    }
//
//    protected void delete(String key) {
//        redisTemplate.delete(key);
//    }
//
//    protected void delete(String... keys) {
//        redisTemplate.delete(Sets.newHashSet(keys));
//    }
//
//    protected void delete(Collection<String> keys) {
//        redisTemplate.delete(keys);
//    }
//
//    protected boolean hasKey(String key) {
//        return redisTemplate.hasKey(key);
//    }
//
//    protected boolean expire(String key, long timeout, TimeUnit unit) {
//        return redisTemplate.expire(key, timeout, unit);
//    }
//
//    protected void hmset(String key, Map<String, Object> hash) {
//        redisTemplate.opsForHash().putAll(key, hash);
//    }
//
//    protected void hset(String key, String hashKey, Object hashValue) {
//        redisTemplate.opsForHash().put(key, hashKey, hashValue);
//    }
//
//    protected Object hget(String key, String hashKey) {
//        return redisTemplate.opsForHash().get(key, hashKey);
//    }
//
//    protected Set<Object> hgetall(String key) {
//        return redisTemplate.opsForHash().keys(key);
//    }
//
//    protected Long hdel(String key, Object... hashKeys) {
//        return redisTemplate.opsForHash().delete(key, hashKeys);
//    }
//
//    protected Long sadd(String key, String... value) {
//        return redisTemplate.opsForSet().add(key, value);
//    }
//
//    protected boolean sismember(String key, String value) {
//        return redisTemplate.opsForSet().isMember(key, value);
//    }
//
//    protected Long srem(String key, Object... values) {
//        return redisTemplate.opsForSet().remove(key, values);
//    }
//
//    protected Set<String> smembers(String key) {
//        return redisTemplate.opsForSet().members(key);
//    }
//
//    protected Long pfadd(String key, String... values) {
//        return redisTemplate.opsForHyperLogLog().add(key, values);
//    }
//
//    protected Long pfcount(String... keys) {
//        return redisTemplate.opsForHyperLogLog().size(keys);
//    }
//
//    public Long addValueToQueue(String key, String value) {
//        return redisTemplate.opsForList().leftPush(key, value);
//    }
//
//    public String getQueuePop(String key) {
//        return redisTemplate.opsForList().rightPop(key);
//    }
//
//    public Long getQueueSize(String key) {
//        return redisTemplate.opsForList().size(key);
//    }

    public Set<String> getScanKeySet(String pattern) {
        log.debug("Scan all Redis key sets, pattern is {}", pattern);
        Set<String> result = null;
        try {
            RedisCallback<Set<String>> callback = (connection) -> {
                Set<String> keysSet = Sets.newHashSet();
                Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(pattern).count(1000).build());
                while (cursor.hasNext()) {
                    keysSet.add(SafeEncoder.encode(cursor.next()));
                }
                return keysSet;
            };
            result = redisTemplate.execute(callback);
        } catch (Exception e) {
            log.error("Scan all Redis error", e);
        }
        log.debug("Scan all Redis key sets complete");
        return result;
    }

    public boolean setLock(String name, String id, int timeout) {
        boolean flag = false;
        String key = joiner.join(CacheKey.LOCK, name);
        try {
            RedisCallback<String> callback = (connection) -> {
                JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                return commands.set(key, id, SetParams.setParams().nx().ex(timeout));
            };
            String result = redisTemplate.execute(callback);
            flag = Optional.ofNullable(result).orElse(StringUtils.EMPTY).equalsIgnoreCase("OK");
        } catch (Exception e) {
            log.error("Setting Redis lock error", e);
        }
        log.debug("Setting Redis lock, Key {} State {}", key, flag);
        return flag;
    }

    public boolean setLock(String name, String id) {
        // Release the lock in 5 minutes
        return setLock(name, id, 300);
    }

    public boolean releaseLock(String name, String id) {
        boolean flag = false;
        String key = joiner.join(CacheKey.LOCK, name);
        try {
            RedisCallback<Long> callback = (connection) -> {
                Object nativeConnection = connection.getNativeConnection();
                // for Redis Cluster
                if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection).eval(LUA_SCRIPT, 1, key, id);
                }
                // for Single Redis
                if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection).eval(LUA_SCRIPT, 1, key, id);
                }
                return 0L;
            };
            flag = redisTemplate.execute(callback) > 0;
        } catch (Exception e) {
            log.error("Releasing Redis lock error", e);
        }
        log.debug("Releasing Redis lock, Key {} State {}", key, flag);
        return flag;
    }

    public long addBlacklist(String tokenId) {
        Long length = redisTemplate.opsForSet().add("TOKEN_BLACKLIST", tokenId);
        if (redisTemplate.getExpire("TOKEN_BLACKLIST") == -1) {
            redisTemplate.expireAt("TOKEN_BLACKLIST", DateTime.now().plusDays(1).toDate());
        }
        return length;
    }

    public boolean hasBlacklist(String tokenId) {
        return redisTemplate.opsForSet().isMember("TOKEN_BLACKLIST", tokenId);
    }

}