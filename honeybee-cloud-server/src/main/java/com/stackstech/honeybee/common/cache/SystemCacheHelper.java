package com.stackstech.honeybee.common.cache;

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
