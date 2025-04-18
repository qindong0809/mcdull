package io.gitee.dqcer.mcdull.framework.redis.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.redis.aspect.CacheExpireAspect;
import io.gitee.dqcer.mcdull.framework.redis.aspect.RedisLockAspect;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * redis 配置
 *
 * @author dqcer
 * @since 2021/09/10
 */
@Configuration
public class RedissonAutoConfiguration extends CachingConfigurerSupport {

    @Bean
    @ConditionalOnMissingBean(CacheChannel.class)
    public CacheChannel getRedissonObject() {
        return new CacheChannel();
    }

    @Bean
    public RedisLockAspect getRedisLockAspect() {
        return new RedisLockAspect();
    }

    @Bean
    public CacheExpireAspect getCacheExpireAspect() {
        return new CacheExpireAspect();
    }


    @Bean
    public RedissonClient redissonClient(RedisProperties properties) {
        Config config = new Config();
        // 默认编码为性能最优 可视化可以用 setCodec(new StringCodec());
        config.setCodec(new StringCodec());
        config.useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setPassword(properties.getPassword())
                .setConnectionPoolSize(50)
                .setDatabase(properties.getDatabase());
        return Redisson.create(config);
//        return null;
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
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(GlobalConstant.CAFFEINE_CACHE);

        Caffeine<Object, Object> caffeineBuilder = Caffeine.newBuilder()
                .maximumSize(1000) // 设置最大缓存项数
                .expireAfterWrite(10, TimeUnit.MINUTES); // 设置写入后过期时间
        cacheManager.setCaffeine(caffeineBuilder);
        return cacheManager;
    }

    /**
     * key生成器，利用","拼接
     * <p>
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
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> Stream.of(params).map(String::valueOf).collect(Collectors.joining(","));
    }
}
