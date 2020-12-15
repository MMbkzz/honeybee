package com.stackstech.dcp.connector.redis;

import com.stackstech.dcp.connector.core.AbstractJedisDataSourceSession;
import com.stackstech.dcp.connector.core.ResourceSession;
import com.stackstech.dcp.connector.core.entity.DriverDataModel;
import com.stackstech.dcp.connector.core.entity.DriverMetaData;
import com.stackstech.dcp.connector.core.entity.DriverModel;
import com.stackstech.dcp.connector.core.enums.MetaDataTypeEnum;
import com.stackstech.dcp.connector.redis.execute.RedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class RedisSession extends AbstractJedisDataSourceSession implements ResourceSession {

    private Jedis jedisTemplate;  //操作对象模板

    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    private RedisSession(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public static RedisSession getInstance(ClassLoader classLoader) {
        return new RedisSession(classLoader);
    }

    public RedisSession initialize(Map<String, Object> config) {

        dataSourceConfig = new JedisPoolConfig();
        dataSourceConfig.setMaxIdle(Integer.valueOf((String) config.get("redis.datasource.max-idle")));
        dataSourceConfig.setMinIdle(Integer.valueOf((String) config.get("redis.datasource.min-idle")));
        dataSourceConfig.setMaxTotal(Integer.valueOf((String) config.get("redis.datasource.max-total")));
        if (config.get("redis.datasource.maxwait") != null) {
            dataSourceConfig.setMaxWaitMillis(Integer.valueOf((String) config.get("redis.datasource.maxwait")) * 1000);
        }
        String redisPassword = (String) config.get("redis.datasource.password");
        dataSource = new JedisPool(
                dataSourceConfig
                , (String) config.get("redis.datasource.host")
                , Integer.valueOf((String) config.get("redis.datasource.port"))
                , Integer.valueOf((String) config.get("redis.datasource.timeout"))
                , redisPassword //
                , Integer.valueOf((String) config.get("redis.datasource.database"))
        );

        jedisTemplate = dataSource.getResource();
        return this;
    }

    @Override
    public List<Map<String, Object>> get(String statement) {

        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put(statement, hgetAll(statement));
        list.add(hashMap);
        return list;
    }

    @Override
    public DriverMetaData get(DriverModel driverModel) {
        DriverDataModel model = (DriverDataModel) driverModel;
        //key 类型
        Map<String, Object> typeValueMap = (Map<String, Object>) model.getRequestData();

        List<Map<String, Object>> valueFields = (List<Map<String, Object>>) typeValueMap.get("special");
        String type = null;//(String)valueFields.get("type");
        for (Map<String, Object> m : valueFields) {

            if ("value".equals(m.get("field"))) {
                type = (String) m.get("value");
            }
        }

        DriverMetaData result = null;
        Jedis jedisTemplate = null;
        try {
            jedisTemplate = dataSource.getResource();
            result = new DriverMetaData(MetaDataTypeEnum.NOSQL, new RedisAdapter(type).execute(jedisTemplate, driverModel));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedisTemplate.close();
        }
        return result;
    }

    public String get2(String key) {
        return jedisTemplate.get(key);
    }

    public Map<String, String> hgetAll(String key) {
        return jedisTemplate.hgetAll(key);
    }

    @Override
    public DriverMetaData put(DriverModel driverModel) {
        return null;
    }

    @Override
    public boolean valid() {
        return true;
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
