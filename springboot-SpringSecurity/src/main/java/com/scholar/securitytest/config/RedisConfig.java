package com.scholar.securitytest.config;

import com.scholar.securitytest.utils.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration  // 标识这是一个配置类
public class RedisConfig {

    // 定义 RedisTemplate Bean
    @Bean
    @SuppressWarnings(value = {"unchecked", "rawtypes"})  // 抑制类型检查警告
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);  // 设置连接工厂，连接 Redis 数据库

        // 自定义 FastJsonRedisSerializer 用于对象序列化和反序列化
        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);

        // 设置 RedisTemplate 的 key 和 value 序列化方式
        // 使用 StringRedisSerializer 来序列化 Redis 的 key 值
        template.setKeySerializer(new StringRedisSerializer());
        
        // 使用 FastJsonRedisSerializer 来序列化 Redis 的 value 值
        template.setValueSerializer(serializer);
        
        // 对于 Hash 类型的 Redis 数据结构，key 使用 StringRedisSerializer，value 使用 FastJsonRedisSerializer
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        // 完成属性设置
        template.afterPropertiesSet();
        return template;  // 返回 RedisTemplate 实例
    }
}
