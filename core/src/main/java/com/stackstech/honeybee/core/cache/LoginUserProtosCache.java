package com.stackstech.honeybee.core.cache;

import com.stackstech.honeybee.core.constants.CacheKeyConstant;
import com.stackstech.honeybee.core.model.LoginUserProtos;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginUserProtosCache extends AbstractRedisStringCache<LoginUserProtos.LoginUser> {

    //private final long keyExpireSecond = 2 * 24 * 60 * 60 * 1000;

    @Override
    public String output(LoginUserProtos.LoginUser value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return StringUtils.EMPTY;
    }

    @Override
    public LoginUserProtos.LoginUser input(String value) {
        try {
            return objectMapper.readValue(value, LoginUserProtos.LoginUser.class);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public String getKey(String key) {
        return CacheKeyConstant.getLoginUserToken(key);
    }
}
