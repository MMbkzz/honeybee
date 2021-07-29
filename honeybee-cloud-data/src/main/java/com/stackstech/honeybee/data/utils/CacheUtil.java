package com.stackstech.honeybee.data.utils;


import com.google.common.base.Joiner;
import com.stackstech.honeybee.data.core.enums.Constant;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public final class CacheUtil {


    public static Joiner joiner = Joiner.on(Constant.SEPARATOR).skipNulls();

//    @Autowired
    @Resource
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