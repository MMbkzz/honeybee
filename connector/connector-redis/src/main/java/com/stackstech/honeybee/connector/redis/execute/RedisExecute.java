package com.stackstech.honeybee.connector.redis.execute;

import com.stackstech.dcp.connector.core.entity.DriverModel;
import redis.clients.jedis.Jedis;

public interface RedisExecute {

    Object execute(Jedis jedisTemplate, DriverModel driverModel);

}
