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

    private List<String> pager(int pageStart, int pageSize) {
        String setsKey = joiner.join(KEY_PREFIX, CacheKey.DATACACHE);

        long min = DateTime.now().getMillis() - applicationConfig.getDataCacheExpires() * 1000;
        long max = DateTime.now().getMillis() + applicationConfig.getDataCacheExpires() * 1000;
        Set<Object> keysSet = redisTemplate.opsForZSet().rangeByScore(setsKey, min, max);

        if (keysSet == null || keysSet.size() == 0) {
            return Collections.emptyList();
        }

        List<String> temp = keysSet.stream().map(Object::toString).collect(Collectors.toList());
        int count = temp.size();
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
        return temp.subList(startIndex, endIndex);
    }


    public List<DataCacheEntity> get(int pageStart, int pageSize) {
        String setsKey = joiner.join(KEY_PREFIX, CacheKey.DATACACHE);
        List<String> list = pager(pageStart, pageSize);
        List<DataCacheEntity> result = Lists.newArrayList();
        for (String k : list) {
            if (redisTemplate.hasKey(k)) {
                result.add((DataCacheEntity) redisTemplate.opsForValue().get(k));
            } else {
                redisTemplate.opsForZSet().remove(setsKey, k);
            }
        }
        return result;
    }

    public int getTotalCount() {
        String setsKey = joiner.join(KEY_PREFIX, CacheKey.DATACACHE);
        long min = DateTime.now().getMillis() - applicationConfig.getDataCacheExpires() * 1000;
        long max = DateTime.now().getMillis() + applicationConfig.getDataCacheExpires() * 1000;
        return redisTemplate.opsForZSet().count(setsKey, min, max).intValue();
    }

}
