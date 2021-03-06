package com.stackstech.honeybee.data.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedisCacheConfig {

    private String host;
    private String port;
    private String database;
    private String password;
    private int maxIdle;
    private int minIdle;
    private int maxActive;
    private int maxWait;
    private int timeout;


//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    public RedisTemplate<String, Object> redisTemplater(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);
        // key & value serializer
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // hash serializer
        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

}
