package com.dqcer.mcdull.framework.web.feign.service;

import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.framework.web.feign.model.LogDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "mcdull-mdc-provider",  contextId = "commonLog")
public interface LogFeignClient {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result < EnumVO >}
     */
    @PostMapping(GlobalConstant.FEIGN_PREFIX + "/log/batch/save")
    Result<Integer> batchSave(@RequestBody List<LogDTO> dto);
}
