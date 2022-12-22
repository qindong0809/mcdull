package com.dqcer.mcdull.framework.web.transform;

import com.dqcer.framework.base.annotation.ITransformer;
import com.dqcer.framework.base.storage.UserContextHolder;
import com.dqcer.framework.base.wrapper.FeignResultParse;
import com.dqcer.mcdull.framework.redis.operation.CacheChannel;
import com.dqcer.mcdull.framework.web.feign.model.DictLiteDTO;
import com.dqcer.mcdull.framework.web.feign.model.DictVO;
import com.dqcer.mcdull.framework.web.feign.service.DictFeignClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Component
public class DictITransformer implements ITransformer<Object> {

    private final DictFeignClient dictFeignClient;

    @Resource
    private CacheChannel cacheChannel;

    public DictITransformer(DictFeignClient dictFeignClient) {
        this.dictFeignClient = dictFeignClient;
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
    public String transform(Object original, Class datasource, String param) {
        DictLiteDTO dto = new DictLiteDTO();
        dto.setCode(String.valueOf(original));
        dto.setSelectType(param);
        dto.setLanguage(UserContextHolder.getSession().getLanguage());
        String key = MessageFormat.format("framework:web:transform:dict:one:{0}:{1}:{2}",
                dto.getSelectType(), dto.getLanguage(), dto.getCode());
        DictVO o = cacheChannel.get(key, DictVO.class);
        if (o != null) {
            return o.getName();
        }

        DictVO vo = FeignResultParse.getInstance(dictFeignClient.detail(dto));

        cacheChannel.put(key, vo, 3000);
        return vo.getName();
    }
}
