package com.dqcer.mcdull.framework.redis.config;

import com.dqcer.mcdull.framework.redis.annotation.ExpireRedisCacheWriter;
import com.dqcer.mcdull.framework.redis.operation.CacheChannel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * redis 配置
 *
 * @author dqcer
 * @version 2021/09/10
 */
@Configuration
public class RedissonAutoConfiguration extends CachingConfigurerSupport {

    @Value("${spring.application.name:unknown}")
    private String appName;


    @Bean
    @ConditionalOnMissingBean(CacheChannel.class)
    public CacheChannel getRedissonObject() {
        return new CacheChannel();
    }


    /**
     * 注入RedisTemplate
     * tips：泛型可以适用于各种类型的注入
     *
     * @param factory         工厂
     * @param redisSerializer redis 序列化器
     * @return {@link RedisTemplate}<{@link String}, {@link T}>
     */
    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory, RedisSerializer<?> redisSerializer) {
        // 指定序列化方式
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    /**
     * 初始化Redis序列器，使用jackson
     *
     * @return {@link RedisSerializer}<{@link Object}>
     */
    @Bean
    public RedisSerializer<Object> redisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        // jdk8日期格式支持
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Module timeModule = new JavaTimeModule()
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(timeFormatter))
                .addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(timeFormatter));
        objectMapper.registerModule(timeModule);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory, RedisSerializer<Object> redisSerializer) {
        RedisCacheConfiguration config = RedisCacheConfiguration
                // 注意默认配置允许缓存null
                .defaultCacheConfig()
                // 接口缓存上指定了key的时候统一加服务名前缀
                .computePrefixWith(cacheName -> appName + ":" + cacheName + ":")
                // 可以根据业务需要设置统一过期时间，这里是为了强制使用@CacheExpire手动设置过期时间所以设置很短
                .entryTtl(Duration.ofMinutes(1))
                // 配置序列化和反序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
        return RedisCacheManager.builder(new ExpireRedisCacheWriter(factory)).cacheDefaults(config).build();
    }

    /**
     * key生成器，利用","拼接
     *
     * 使用@Cachable生成的key的默认规则
     * 如果手动指定了key则不生效
     * <p>
     * 格式：入参1,入参2
     * 示例：1,2
     * 加上前缀后完整key格式：order_service:order:1,2
     * <p>
     * 建议使用@Cacheable的时候都指定key
     *
     * @return {@link KeyGenerator}
     */
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> Stream.of(params).map(String::valueOf).collect(Collectors.joining(","));
    }
}
