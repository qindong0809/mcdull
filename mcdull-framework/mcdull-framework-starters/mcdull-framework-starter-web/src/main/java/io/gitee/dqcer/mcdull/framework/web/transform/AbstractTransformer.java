package io.gitee.dqcer.mcdull.framework.web.transform;

import io.gitee.dqcer.mcdull.framework.base.annotation.ITransformer;
import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.mcdull.framework.redis.operation.CacheChannel;

import javax.annotation.Resource;
import java.text.MessageFormat;

/**
 * Abstract Transformer
 *
 * @author dqcer
 * @since 2022/12/23
 */
public abstract class AbstractTransformer implements ITransformer<Object> {

    @Resource
    private CacheChannel cacheChannel;

    private String keyFormat() {
        return "framework:web:transform:{0}:{0}:{1}";
    }

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
        String keyFormat = keyFormat();
        String cacheName = cacheName();
        if (cacheName == null) {
            throw new BusinessException("继承 AbstractTransformer 类时， cacheName is not null");
        }
        String key = MessageFormat.format(keyFormat, cacheName, original, param);

        KeyValueBO<?, ?> bo = cacheChannel.get(key, KeyValueBO.class);
        if (bo != null) {
            return String.valueOf(bo.getValue());
        }

        bo = getKeyValueVO(original, param);
        cacheChannel.put(key, bo, cacheExpireTime());
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
