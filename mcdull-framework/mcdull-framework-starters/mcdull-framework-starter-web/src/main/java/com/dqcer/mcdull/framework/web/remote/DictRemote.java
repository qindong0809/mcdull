package com.dqcer.mcdull.framework.web.remote;

import com.dqcer.framework.base.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "mcdull-mdc-provider",  contextId = "commonDict")
public interface DictRemote {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result < DictVO >}
     */
    @GetMapping("feign/dict/detail")
    Result<DictVO> detail(@SpringQueryMap @Validated(value = ValidGroup.One.class) DictLiteDTO dto);
}
