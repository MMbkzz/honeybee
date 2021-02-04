package com.stackstech.honeybee.server.core.conf;

import com.stackstech.honeybee.server.core.handler.KryoRedisSerializer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis cache config
 *
 * @author william
 * @since 1.0
 */
@RefreshScope
@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // key & value serializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new KryoRedisSerializer<>(Object.class));
        // hash serializer
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new KryoRedisSerializer<>(Object.class));
        //
        template.afterPropertiesSet();

        return template;
    }

}
