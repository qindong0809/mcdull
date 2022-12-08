package com.dqcer.mcdull.framework.web.remote;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.dto.SysLogFeignDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "mcdull-mdc-provider",  contextId = "commonLog")
public interface RemoteLogService {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result < EnumVO >}
     */
    @PostMapping("feign/log/batch/save")
    Result<Integer> batchSave(@RequestBody List<SysLogFeignDTO> dto);
}
