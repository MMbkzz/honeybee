package com.stackstech.honeybee.common.cache;

import com.google.common.collect.Lists;
import com.stackstech.honeybee.server.core.conf.ApplicationConfig;
import com.stackstech.honeybee.server.core.enums.CacheKey;
import com.stackstech.honeybee.server.system.entity.DataCacheEntity;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DataCacheHelper extends AbstractCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ApplicationConfig applicationConfig;

    public void add(DataCacheEntity entity) {
        String entityKey = joiner.join(KEY_PREFIX, CacheKey.DATACACHE, entity.getUuid());
        redisTemplate.opsForValue().set(entityKey, entity, applicationConfig.getDataCacheExpires(), TimeUnit.SECONDS);
        String setsKey = joiner.join(KEY_PREFIX, CacheKey.DATACACHE);
        if (redisTemplate.hasKey(entityKey)) {
            redisTemplate.opsForZSet().add(setsKey, entityKey, DateTime.now().plusSeconds(entity.getExpire()).getMillis());
        }
    }

    public DataCacheEntity get(String uuid) {
        String key = joiner.join(KEY_PREFIX, CacheKey.DATACACHE, uuid);
        if (redisTemplate.hasKey(key)) {
            return (DataCacheEntity) redisTemplate.opsForValue().get(key);
        }
        return null;
    }

    public boolean delete(String uuid) {
        String key = joiner.join(KEY_PREFIX, CacheKey.DATACACHE, uuid);
        redisTemplate.delete(key);
        String setsKey = joiner.join(KEY_PREFIX, CacheKey.DATACACHE);
        return redisTemplate.opsForZSet().remove(setsKey, key) > 0;
    }

    private Set<String> scan(String keywords) {
        String pattern = joiner.join(KEY_PREFIX, CacheKey.DATACACHE, keywords);
        return super.getScanKeySet(redisTemplate, pattern);
    }

    private List<String> pager(List<String> keys, int pageStart, int pageSize) {
        int count = keys.size();
        int startIndex = (pageStart - 1) * pageSize;
        if (startIndex >= count) {
            return Collections.emptyList();
        }
        if (startIndex <= 0) {
            startIndex = 0;
        }
        int endIndex = pageStart * pageSize;
        if (endIndex >= count) {
            endIndex = count;
        }
        return keys.subList(startIndex, endIndex);
    }

    public List<DataCacheEntity> get(String keywords, int pageStart, int pageSize) {
        List<String> temp = Lists.newArrayList(scan(keywords));
        List<String> elements = pager(temp, pageStart, pageSize);

        List<DataCacheEntity> result = Lists.newArrayList();
        for (String k : elements) {
            if (redisTemplate.hasKey(k)) {
                result.add((DataCacheEntity) redisTemplate.opsForValue().get(k));
            }
        }
        return result;
    }

    public List<DataCacheEntity> get(int pageStart, int pageSize) {
        String setsKey = joiner.join(KEY_PREFIX, CacheKey.DATACACHE);

        Set<Object> keysSet = redisTemplate.opsForZSet().range(setsKey, 0, -1);
        if (keysSet == null || keysSet.size() == 0) {
            return Collections.emptyList();
        }

        List<String> temp = keysSet.stream().map(Object::toString).collect(Collectors.toList());
        List<String> elements = pager(temp, pageStart, pageSize);

        List<DataCacheEntity> result = Lists.newArrayList();
        List<String> expires = Lists.newArrayList();
        for (String k : elements) {
            if (redisTemplate.hasKey(k)) {
                result.add((DataCacheEntity) redisTemplate.opsForValue().get(k));
            } else {
                expires.add(k);
            }
        }
        if (expires.size() > 0) {
            redisTemplate.opsForZSet().remove(setsKey, expires.toArray());
        }
        return result;
    }

    public int getTotalCount(String keywords) {
        return scan(keywords).size();
    }

    public int getTotalCount() {
        return redisTemplate.opsForZSet().zCard(joiner.join(KEY_PREFIX, CacheKey.DATACACHE)).intValue();
    }

}
