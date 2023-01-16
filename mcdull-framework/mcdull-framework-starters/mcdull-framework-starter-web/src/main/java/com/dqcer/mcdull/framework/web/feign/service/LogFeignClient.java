package com.dqcer.mcdull.framework.web.feign.service;

import com.dqcer.mcdull.framework.base.constants.GlobalConstant;
import com.dqcer.mcdull.framework.base.wrapper.Result;
import com.dqcer.mcdull.framework.web.feign.model.LogOperationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 日志 对外调用客户端
 *
 * @author dqcer
 * @version 2022/12/26
 */
@FeignClient(value = "mcdull-mdc-provider",  contextId = "commonLog")
public interface LogFeignClient {

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result < EnumVO >}
     */
    @PostMapping(GlobalConstant.INNER_API + "/log/batch/save")
    Result<Integer> batchSave(@RequestBody List<LogOperationDTO> dto);
}
