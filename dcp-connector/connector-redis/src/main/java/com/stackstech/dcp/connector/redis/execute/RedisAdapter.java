package com.stackstech.dcp.connector.redis.execute;

import com.stackstech.dcp.connector.core.entity.DriverModel;
import com.stackstech.dcp.connector.redis.execute.impl.HashGetExecute;
import com.stackstech.dcp.connector.redis.execute.impl.ListGetExecute;
import com.stackstech.dcp.connector.redis.execute.impl.SetGetExecute;
import com.stackstech.dcp.connector.redis.execute.impl.StringGetExecute;
import redis.clients.jedis.Jedis;

/**
 *
 */
public class RedisAdapter {

    RedisExecute redisExecute = null;

    public RedisAdapter(String type) {
        if ("string".equals(type.toLowerCase())) {
            redisExecute = new StringGetExecute();
        } else if ("hash".equals(type.toLowerCase())) {
            redisExecute = new HashGetExecute();
        } else if ("list".equals(type.toLowerCase())) {
            redisExecute = new ListGetExecute();
        } else if ("set".equals(type.toLowerCase())) {
            redisExecute = new SetGetExecute();
        }
    }

    public Object execute(Jedis jedisTemplate, DriverModel driverModel) {
        return redisExecute.execute(jedisTemplate, driverModel);
    }
}
