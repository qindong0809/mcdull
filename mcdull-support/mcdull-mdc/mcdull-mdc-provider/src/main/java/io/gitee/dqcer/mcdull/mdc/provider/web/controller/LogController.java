package io.gitee.dqcer.mcdull.mdc.provider.web.controller;


import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.mdc.client.vo.DictTypeClientVO;
import io.gitee.dqcer.mcdull.mdc.provider.model.dto.LogLiteDTO;
import io.gitee.dqcer.mcdull.mdc.provider.model.vo.LogVO;
import io.gitee.dqcer.mcdull.mdc.provider.web.service.LogService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 日志控制器
 *
 * @author dqcer
 * @since 2022/12/25
 */
@RestController
public class LogController {

    @Resource
    private LogService logService;

    /**
     * 列表分页
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link DictTypeClientVO}>>
     */
    @GetMapping("/log/base/list")
    public Result<PagedVO<LogVO>> listByPage(@Validated(ValidGroup.Paged.class) LogLiteDTO dto) {
        return logService.listByPage(dto);
    }
}
