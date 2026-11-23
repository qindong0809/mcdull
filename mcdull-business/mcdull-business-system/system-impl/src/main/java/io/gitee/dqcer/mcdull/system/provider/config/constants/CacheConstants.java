package io.gitee.dqcer.mcdull.system.provider.config.constants;

/**
 * 缓存常量
 *
 * @author dqcer
 * @since 2022/12/24
 */
public final class CacheConstants {

    /**
     * 业务缓存名称。历史上对应本地 Caffeine 缓存，现已统一为 Redis，
     * 保留原值 "caffeineCache" 以兼容既有缓存 key，避免前缀变化。
     */
    public static final String BUSINESS_CACHE = "caffeineCache";

    /**
     * 码表缓存
     */
    public static final String DICT_LIST = "mcdull:uac:dict_key:{}:dict_value";
    public static final Long DICT_EXPIRE = 60 * 60 * 24L;
}
