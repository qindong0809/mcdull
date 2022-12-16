package com.dqcer.mcdull.framework.web.transform;

import com.dqcer.framework.base.annotation.ITransformer;
import com.dqcer.framework.base.storage.UserContextHolder;
import com.dqcer.framework.base.wrapper.FeignResultParse;
import com.dqcer.mcdull.framework.redis.operation.CacheChannel;
import com.dqcer.mcdull.framework.web.remote.DictLiteDTO;
import com.dqcer.mcdull.framework.web.remote.DictRemote;
import com.dqcer.mcdull.framework.web.remote.DictVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Component
public class DictITransformer implements ITransformer {

    private static final Logger log = LoggerFactory.getLogger(DictITransformer.class);

    @Resource
    private DictRemote dictRemote;

    @Resource
    private CacheChannel cacheChannel;


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
            log.debug("码表翻译处理 original: {} datasource: {} param: {}", original, datasource.getName(), param);
        }
        DictLiteDTO dto = new DictLiteDTO();
        dto.setCode(String.valueOf(original));
//        dto.setSelectType(param);
        dto.setLanguage(UserContextHolder.getSession().getLanguage());
        String key = MessageFormat.format("framework:web:transform:dict:one:{0}:{1}:{2}",
                dto.getSelectType(), dto.getLanguage(), dto.getCode());
        DictVO o = cacheChannel.get(key, DictVO.class);
        if (o != null) {
            return o.getName();
        }

        DictVO vo = FeignResultParse.getInstance(dictRemote.detail(dto));

        cacheChannel.put(key, vo, 3000);
        return vo.getName();
    }
}
