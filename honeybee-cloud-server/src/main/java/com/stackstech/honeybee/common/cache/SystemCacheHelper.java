package com.stackstech.honeybee.common.cache;

import com.stackstech.honeybee.server.core.enums.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SystemCacheHelper extends AbstractCacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public long addBlacklist(String tokenId) {
        Long length = redisTemplate.opsForSet().add(CacheKey.TOKEN_BLACKLIST.name(), tokenId);
        if (redisTemplate.getExpire(CacheKey.TOKEN_BLACKLIST.name()) == -1) {
            redisTemplate.expireAt(CacheKey.TOKEN_BLACKLIST.name(), DateTime.now().plusDays(1).toDate());
        }
        return length;
    }

    public boolean hasBlacklist(String tokenId) {
        return redisTemplate.opsForSet().isMember(CacheKey.TOKEN_BLACKLIST.name(), tokenId);
    }
}
