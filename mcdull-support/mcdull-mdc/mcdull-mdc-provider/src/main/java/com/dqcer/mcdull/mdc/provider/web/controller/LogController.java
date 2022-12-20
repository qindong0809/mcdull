package com.dqcer.mcdull.mdc.provider.web.controller;


import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.mdc.client.vo.DictClientVO;
import com.dqcer.mcdull.mdc.provider.model.dto.LogLiteDTO;
import com.dqcer.mcdull.mdc.provider.model.vo.LogVO;
import com.dqcer.mcdull.mdc.provider.web.service.LogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class LogController {

    @Resource
    private LogService logService;

    /**
     * 列表分页
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictClientVO}>>
     */
    @GetMapping("/log/base/list")
    public Result<PagedVO<LogVO>> listByPage(@Validated(ValidGroup.Paged.class) LogLiteDTO dto) {
        return logService.listByPage(dto);
    }
}
