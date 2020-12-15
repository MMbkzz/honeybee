package com.stackstech.honeybee.connector.redis.execute.impl;

import com.stackstech.honeybee.connector.core.entity.DriverModel;
import com.stackstech.honeybee.connector.redis.execute.RedisExecute;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class StringGetExecute implements RedisExecute {
    @Override
    public Object execute(Jedis jedisTemplate, DriverModel driverModel) {
        Map<String, Object> typeValueMap = (Map<String, Object>) driverModel.getRequestData();
        List<Map<String, Object>> valueFields = (List<Map<String, Object>>) typeValueMap.get("special");

        String key = null;
        for (Map<String, Object> m : valueFields) {
            if ("key".equals(m.get("field"))) {
                key = (String) m.get("value");
            }
        }
        Object resultString = null;

        try {
            resultString = jedisTemplate.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultString;
    }
}
