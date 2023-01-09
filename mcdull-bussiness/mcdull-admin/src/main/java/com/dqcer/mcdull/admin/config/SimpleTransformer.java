package com.dqcer.mcdull.admin.config;

import com.dqcer.mcdull.framework.web.feign.model.DictLiteDTO;
import com.dqcer.mcdull.framework.web.feign.model.DictVO;
import com.dqcer.mcdull.framework.web.transform.DictTransformer;
import org.springframework.stereotype.Component;

@Component
public class SimpleTransformer extends DictTransformer {



    @Override
    public DictVO getVO(DictLiteDTO dto) {
        return super.getVO(dto);
    }
}
