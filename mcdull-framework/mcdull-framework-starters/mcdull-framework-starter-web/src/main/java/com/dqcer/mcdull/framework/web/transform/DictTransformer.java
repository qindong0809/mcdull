package com.dqcer.mcdull.framework.web.transform;

import com.dqcer.framework.base.auth.UserContextHolder;
import com.dqcer.framework.base.dict.Transformer;
import com.dqcer.framework.base.feign.FeignResultParse;
import com.dqcer.mcdull.framework.web.remote.DictLiteDTO;
import com.dqcer.mcdull.framework.web.remote.DictRemote;
import com.dqcer.mcdull.framework.web.remote.DictVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DictTransformer implements Transformer {

    private static final Logger log = LoggerFactory.getLogger(DictTransformer.class);

    @Resource
    private DictRemote dictRemote;


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
        DictLiteDTO dto = new DictLiteDTO();
        dto.setCode(String.valueOf(original));
        dto.setSelectType(param);
        dto.setLanguage(UserContextHolder.getSession().getLanguage());
        DictVO vo = FeignResultParse.getInstance(dictRemote.detail(dto));
        return vo.getName();
    }
}
