package com.dqcer.mcdull.framework.web.remote;

import com.dqcer.framework.base.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.dto.SysLogDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "mcdull-mdc-provider",  contextId = "commonLog")
public interface RemoteLogService {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result < DictVO >}
     */
    @PostMapping("feign/log/batch/save")
    Result<Integer> batchSave(@RequestBody List<SysLogDTO> dto);
}
