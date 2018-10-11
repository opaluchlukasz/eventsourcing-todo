package com.github.opaluchlukasz.eventsourcingtodo.query;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class ReadDbConfig {
    @Bean
    JedisConnectionFactory jedisConnectionFactory(@Value("${spring.redis.host}") String host,
                                                  @Value("${spring.redis.port}") Integer port) {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(host, port
        ));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        return template;
    }
}
