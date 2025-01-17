package io.gitee.dqcer.mcdull.admin.framework.transformer;

import io.gitee.dqcer.mcdull.framework.base.bo.KeyValueBO;
import io.gitee.dqcer.mcdull.framework.web.transform.AbstractTransformer;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

/**
 * 用户翻译
 *
 * @author dqcer
 * @since 2023/01/15 17:01:44
 */
@Component
public class UserTransformer extends AbstractTransformer {

    @Resource
    private IUserTransformerService transformer;

    /**
     * 模块缓存名称，比如 user
     *
     * @return {@link String}
     */
    @Override
    protected String cacheName() {
        return "user";
    }

    @Override
    protected KeyValueBO<String, String> getKeyValueVO(Object code, String param) {
        return transformer.transformer(String.valueOf(code));
    }
}
