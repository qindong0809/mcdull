package com.dqcer.mcdull.mdc.provider.web.controller;


import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.api.dto.LogLiteDTO;
import com.dqcer.mcdull.mdc.api.dto.SysLogFeignDTO;
import com.dqcer.mcdull.mdc.api.vo.DictVO;
import com.dqcer.mcdull.mdc.api.vo.LogVO;
import com.dqcer.mcdull.mdc.provider.web.service.LogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
    public Result<Integer> batchSave(@RequestBody List<SysLogFeignDTO> dto){
        return logService.batchSave(dto);
    }

    /**
     * 列表分页
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictVO}>>
     */
    @GetMapping("/log/base/list")
    public Result<PagedVO<LogVO>> listByPage(@Validated(ValidGroup.Paged.class) LogLiteDTO dto) {
        return logService.listByPage(dto);
    }
}
