package io.gitee.dqcer.mcdull.system.provider.config.constants;

/**
 * 缓存常量
 *
 * @author dqcer
 * @since 2022/12/24
 */
public final class CacheConstants {

    /**
     * 业务缓存名称。所有 @Cacheable / @CacheEvict 统一由 RedisCacheManager 落到 Redis，
     * 不再保留历史本地 Caffeine 缓存名。
     */
    public static final String BUSINESS_CACHE = "businessCache";

    /**
     * 码表缓存
     */
    public static final String DICT_LIST = "mcdull:uac:dict_key:{}:dict_value";
    public static final Long DICT_EXPIRE = 60 * 60 * 24L;
}
