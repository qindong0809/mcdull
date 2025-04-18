package io.gitee.dqcer.mcdull.system.provider.web.service;

import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.CacheQueryDTO;

/**
 * Cache service
 *
 * @author qcer
 * @since 2024/05/23
 */
public interface ICacheService {

    /**
     * 缓存名称
     *
     * @param dto DTO
     * @return {@link PagedVO }<{@link KeyValueVO }<{@link String }, {@link String }>>
     */
    PagedVO<KeyValueVO<String, String>> queryPage(CacheQueryDTO dto);

    /**
     * 删除缓存
     *
     * @param caffeineCache 咖啡因缓存
     * @param name          名字
     * @return boolean
     */
    boolean removeCache(Boolean caffeineCache, String name);

    /**
     * 缓存键
     *
     * @param caffeineCacheFlag 咖啡因缓存标志
     * @param cacheName         缓存名称
     * @return {@link Object }
     */
    Object cacheKey(Boolean caffeineCacheFlag, String cacheName);

    /**
     * 导出数据
     *
     * @param dto DTO
     * @return boolean
     */
    boolean exportData(CacheQueryDTO dto);
}
