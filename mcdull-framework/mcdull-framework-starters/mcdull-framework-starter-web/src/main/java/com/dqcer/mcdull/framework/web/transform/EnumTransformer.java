package com.dqcer.mcdull.framework.web.transform;

import com.dqcer.framework.base.annotation.ITransformer;
import com.dqcer.framework.base.enums.IEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * enum类翻译统一处理
 *
 * @author dqcer
 * @version 2022/10/05
 */
public class EnumTransformer implements ITransformer<Object> {

    private static final Logger log = LoggerFactory.getLogger(EnumTransformer.class);


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
        Class<IEnum<?>> iDict = (Class<IEnum<?>>) datasource;
        for (IEnum<?> dict : iDict.getEnumConstants()) {
            if (dict.getCode().equals(original)) {
                return dict.getText();
            }
        }
        log.error("匹配未成功 original: {}, datasource: {} param: {}", original, datasource, param);
        return null;
    }
}
