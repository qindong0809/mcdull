package com.dqcer.mcdull.uac.provider.config.handler;

import com.dqcer.framework.base.dict.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DictTransformer implements Transformer {

    private static final Logger log = LoggerFactory.getLogger(DictTransformer.class);


    /**
     * 翻译
     *
     * @param original   原始值
     * @param datasource 翻译数据源
     * @param param      翻译后取值参数名字
     * @return 翻译后的值
     */
    @Override
    public String transform(Object original, Class datasource, String param) {
        if (log.isDebugEnabled()) {
            log.debug("Dict transformer original: {} datasource: {} param: {}");
        }
        return null;
    }
}
