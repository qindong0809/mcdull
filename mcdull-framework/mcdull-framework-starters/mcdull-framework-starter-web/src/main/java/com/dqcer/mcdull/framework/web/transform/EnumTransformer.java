package com.dqcer.mcdull.framework.web.transform;

import com.dqcer.framework.base.dict.IDict;
import com.dqcer.framework.base.dict.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * enum类统一处理
 *
 * @author dqcer
 * @version 2022/10/05
 */
public class EnumTransformer implements Transformer<Object> {

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
        Class<IDict<?>> iDict = (Class<IDict<?>>) datasource;
        for (IDict<?> dict : iDict.getEnumConstants()) {
            if (dict.getCode().equals(original)) {
                return dict.getText();
            }
        }
        log.error("匹配未成功 original: {}, datasource: {} param: {}", original, datasource, param);
        return null;
    }
}
