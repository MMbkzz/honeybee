package com.stackstech.honeybee.common.cache;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.stackstech.honeybee.server.core.enums.CacheKey;
import com.stackstech.honeybee.server.core.enums.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.util.SafeEncoder;

import java.util.Optional;
import java.util.Set;

/**
 * Abstract cache service
 *
 * @author william
 * @since 1.0
 */
@Slf4j
public abstract class AbstractCacheService {

    protected static final String LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    protected static final String KEY_PREFIX = Constant.SERVER_NAME.toUpperCase();

    protected static Joiner joiner = Joiner.on(Constant.SEPARATOR).skipNulls();

    public final Set<String> getScanKeySet(RedisTemplate<String, Object> redisTemplate, String pattern) {
        log.debug("Scan all Redis key sets, pattern is {}", pattern);
        Set<String> result = Sets.newLinkedHashSet();
        try {
            RedisCallback<Set<String>> callback = (connection) -> {
                Set<String> keysSet = Sets.newLinkedHashSet();
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

    public final boolean setLock(RedisTemplate<String, Object> redisTemplate, String name, String id, int timeout) {
        boolean flag = false;
        String key = joiner.join(CacheKey.LOCK, name);
        try {
            RedisCallback<String> callback = (connection) -> {
                JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                return commands.set(key, id, SetParams.setParams().nx().ex(timeout));
            };
            String result = redisTemplate.execute(callback);
            flag = "OK".equalsIgnoreCase(Optional.ofNullable(result).orElse(StringUtils.EMPTY));
        } catch (Exception e) {
            log.error("Setting Redis lock error", e);
        }
        log.debug("Setting Redis lock, Key {} State {}", key, flag);
        return flag;
    }

    public final boolean setLock(RedisTemplate<String, Object> redisTemplate, String name, String id) {
        // Release the lock in 5 minutes
        return setLock(redisTemplate, name, id, 300);
    }

    public final boolean releaseLock(RedisTemplate<String, Object> redisTemplate, String name, String id) {
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

}
