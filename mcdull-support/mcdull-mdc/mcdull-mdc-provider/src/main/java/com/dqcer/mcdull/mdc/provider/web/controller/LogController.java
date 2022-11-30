package com.dqcer.mcdull.mdc.provider.web.controller;


import com.dqcer.framework.base.ValidGroup;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.dto.SysLogDTO;
import com.dqcer.mcdull.mdc.provider.web.service.LogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class LogController {

    @Resource
    private LogService logService;

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result <Long> 返回新增主键}
     */
    @PostMapping("feign/log/batch/save")
    public Result<Integer> batchSave(@RequestBody @Validated(value = {ValidGroup.Add.class}) List<SysLogDTO> dto){
        return logService.batchSave(dto);
    }
}
