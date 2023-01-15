package com.dqcer.mcdull.admin.framework.transformer;

import com.dqcer.framework.base.vo.KeyValueVO;

/**
 * 翻译转换接口
 *
 * @author dqcer
 * @version 2023/01/15 15:01:68
 */
public interface IDictTransformerService {

    /**
     * 翻译
     *
     * @param code       代码
     * @param selectType 选择类型
     * @param language   语言
     * @return {@link KeyValueVO}
     */
    KeyValueVO<String, String> transformer(String code, String selectType, String language);
}
