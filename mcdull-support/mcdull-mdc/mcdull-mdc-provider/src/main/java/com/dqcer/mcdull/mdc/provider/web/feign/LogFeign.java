package com.dqcer.mcdull.mdc.provider.web.feign;

import com.dqcer.framework.base.annotation.UnAuthorize;
import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.provider.model.dto.SysLogFeignDTO;
import com.dqcer.mcdull.mdc.provider.web.service.LogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class LogFeign {

    @Resource
    private LogService logService;

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result <Long> 返回新增主键}
     */
    @UnAuthorize
    @PostMapping(GlobalConstant.FEIGN_PREFIX + "/log/batch/save")
    public Result<Integer> batchSave(@RequestBody List<SysLogFeignDTO> dto){
        return logService.batchSave(dto);
    }
}
