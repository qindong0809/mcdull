package com.dqcer.mcdull.framework.web.remote;

import com.dqcer.framework.base.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "mcdull-mdc-provider",  contextId = "commonDict")
public interface DictRemote {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result < DictVO >}
     */
    @GetMapping("/dict/detail")
    Result<DictVO> one(@RequestBody @Validated(value = ValidGroup.One.class) DictLiteDTO dto);
}
