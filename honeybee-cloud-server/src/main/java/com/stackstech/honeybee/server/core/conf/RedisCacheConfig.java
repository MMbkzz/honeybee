package com.stackstech.honeybee.server.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Data
@RefreshScope
@ConfigurationProperties(prefix = "app.datasource.redis")
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

    @Bean
    public RedisTemplate<String, Object> redisTemplater(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // key & value serializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // hash serializer
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

}
