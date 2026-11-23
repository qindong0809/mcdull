package io.gitee.dqcer.mcdull.framework.redis.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import io.gitee.dqcer.mcdull.framework.redis.aspect.CacheExpireAspect;
import io.gitee.dqcer.mcdull.framework.redis.aspect.RedisLockAspect;
import io.gitee.dqcer.mcdull.framework.redis.jmx.RedisJmxAdapter;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedisClient;
import io.gitee.dqcer.mcdull.framework.redis.operation.RedissonCache;
import io.gitee.dqcer.mcdull.framework.redis.ratelimit.ConcurrentRateLimiterImpl;
import io.gitee.dqcer.mcdull.framework.redis.support.ExpireRedisCacheWriter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * redis 自动配置。引入 starter-redis 即生效，无需在启动类标注任何启用注解。
 *
 * @author dqcer
 * @since 2021/09/10
 */
@AutoConfiguration(after = RedisAutoConfiguration.class)
@ConditionalOnClass({RedissonClient.class, RedisConnectionFactory.class})
@EnableCaching(proxyTargetClass = true)
@Import({RedisClient.class, RedissonCache.class, ConcurrentRateLimiterImpl.class, RedisJmxAdapter.class})
public class RedissonAutoConfiguration implements CachingConfigurer {

    /**
     * 默认缓存时间（分钟）。统一在此处调整，不要散落到业务方法里。
     */
    private static final long DEFAULT_CACHE_TTL_MINUTES = 30;

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
        // 与 Spring Cache 共用同一套 Jackson 序列化，避免同一 Redis 中多种数据格式并存
        config.setCodec(new JsonJacksonCodec(redisObjectMapper()));
        SingleServerConfig singleServer = config.useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setConnectionPoolSize(50)
                .setDatabase(properties.getDatabase());
        String password = properties.getPassword();
        if (password != null && !password.isBlank()) {
            singleServer.setPassword(password);
        }
        return Redisson.create(config);
    }

    /**
     * Redis 序列化器。
     *
     * @return {@link RedisSerializer}<{@link Object}>
     */
    @Bean
    public RedisSerializer<Object> redisSerializer() {
        return new GenericJackson2JsonRedisSerializer(redisObjectMapper());
    }

    /**
     * 统一的 Redis Jackson 配置：日期格式 + 受限多态类型。
     * Redisson codec 与 Spring Cache 序列化器共用本配置，保证同一 Redis 中只有一种数据格式。
     */
    private ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Module timeModule = new JavaTimeModule()
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(timeFormatter))
                .addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(timeFormatter));
        objectMapper.registerModule(timeModule);
        BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("io.gitee.dqcer.mcdull.")
                .allowIfSubType("java.util.")
                .allowIfSubType("java.time.")
                .allowIfSubType("java.math.")
                .allowIfSubType("java.lang.")
                .build();
        objectMapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        return objectMapper;
    }

    /**
     * 分布式缓存管理器。
     * <p>
     * {@code @Cacheable} / {@code @CacheEvict} 全部落到 Redis，多实例共享同一份缓存，
     * 避免 JVM 堆内缓存导致的数据不一致。
     * <p>
     * 写入器用 {@link ExpireRedisCacheWriter}：它在写入时读取 {@code @CacheExpire} 设置的 TTL，
     * 使该注解真正生效（此前切面写入的 {@code CacheExpireHolder} 没有任何消费者）。
     *
     * @param connectionFactory Redisson starter 提供的连接工厂，与 RedissonClient 共用同一实例
     * @return Redis 缓存管理器
     */
    @Primary
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheWriter cacheWriter = new ExpireRedisCacheWriter(connectionFactory);
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                // 默认 30 分钟；需要更短或更长的缓存用 @CacheExpire 在方法上单独指定
                .entryTtl(Duration.ofMinutes(DEFAULT_CACHE_TTL_MINUTES))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()));
        return RedisCacheManager.builder(cacheWriter).cacheDefaults(configuration).build();
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
