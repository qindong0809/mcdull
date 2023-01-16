package io.gitee.dqcer.admin.framework.transformer;

import io.gitee.dqcer.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.framework.base.vo.KeyValueVO;
import io.gitee.dqcer.framework.web.transform.AbstractTransformer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 简单的翻译转换器
 *
 * @author dqcer
 * @version 2023/01/15 15:01:37
 */
@Component
public class DictTransformer extends AbstractTransformer {

    @Resource
    private IDictTransformerService transformer;

    /**
     * 模块缓存名称，比如 user
     *
     * @return {@link String}
     */
    @Override
    protected String cacheName() {
        return "dict";
    }

    @Override
    protected KeyValueVO<String, String> getKeyValueVO(Object code, String param) {
        return transformer.transformer(String.valueOf(code), param, UserContextHolder.getSession().getLanguage());
    }
}
