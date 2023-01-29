package io.gitee.dqcer.mcdull.admin.framework.transformer;

import io.gitee.dqcer.mcdull.framework.base.vo.KeyValueVO;

/**
 * 翻译转换接口
 *
 * @author dqcer
 * @since 2023/01/15 15:01:68
 */
public interface IUserTransformerService {

    /**
     * 翻译
     *
     * @param code       编码
     * @return {@link KeyValueVO}
     */
    KeyValueVO<String, String> transformer(String code);
}
