package io.gitee.dqcer.mcdull.framework.web.transform;

import io.gitee.dqcer.mcdull.framework.base.annotation.ITransformer;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.cache.ICache;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * Abstract Transformer
 *
 * @author dqcer
 * @since 2022/12/23
 */
public abstract class AbstractTransformer implements ITransformer<Object> {

    /**
     * 缓存键模板：模块名 + 原始值 + 取值参数。三者缺一，不同参数的翻译就会互相覆盖。
     */
    private static final String KEY_FORMAT = "framework:web:transform:%s:%s:%s";

    /**
     * 缓存是可选能力：未引入缓存 starter 时降级为每次直查。按名限定是因为
     * {@code caffeineCache} 与 {@code redissonCache} 同时注册为 {@link ICache}，按类型取会撞上多候选。
     */
    @Autowired
    @Qualifier("redissonCache")
    private ObjectProvider<ICache> cacheProvider;

    /**
     * 翻译
     *
     * @param original   原始值
     * @param datasource 翻译数据源
     * @param param      翻译后取值参数名字
     * @return 翻译后的值
     */
    @Override
    public String transform(Object original, Class<?> datasource, String param) {
        String cacheName = cacheName();
        if (cacheName == null) {
            throw new BusinessException("继承 AbstractTransformer 类时， cacheName is not null");
        }
        String key = String.format(KEY_FORMAT, cacheName, original, param);

        ICache cache = cacheProvider.getIfAvailable();
        KeyValueBO<?, ?> bo = cache == null ? null : cache.get(key, KeyValueBO.class);
        if (bo != null) {
            return String.valueOf(bo.getValue());
        }

        bo = getKeyValueVO(original, param);
        if (cache != null) {
            cache.put(key, bo, cacheExpireTime());
        }
        return String.valueOf(bo.getValue());
    }

    /**
     * 缓存过期时间 默认 3s
     *
     * @return long
     */
    protected long cacheExpireTime() {
        return 3000L;
    }

    /**
     * 根据参数进行查询
     *
     * @param code  代码
     * @param param 参数
     * @return {@link KeyValueVO}
     */
    protected abstract KeyValueBO<String, String> getKeyValueVO(Object code, String param);

    /**
     * 模块缓存名称，比如 user
     *
     * @return {@link String}
     */
    protected abstract String cacheName();
}
