package com.dqcer.mcdull.mdc.provider.web.feign;

import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.provider.model.dto.SysLogFeignDTO;
import com.dqcer.mcdull.mdc.provider.web.service.LogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志 feign 实现层
 *
 * @author dqcer
 * @version 2022/12/26
 */
@RestController
public class LogServerFeign {

    @Resource
    private LogService logService;

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result <Long> 返回新增主键}
     */
    @PostMapping(GlobalConstant.INNER_API + "/log/batch/save")
    public Result<Integer> batchSave(@RequestBody List<SysLogFeignDTO> dto){
        return logService.batchSave(dto);
    }
}
