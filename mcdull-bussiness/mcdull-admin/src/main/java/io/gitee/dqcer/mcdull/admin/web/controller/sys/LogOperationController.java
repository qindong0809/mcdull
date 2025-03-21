package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.LogLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.LogVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.ILogService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
 * 日志操作 控制器
 *
 * @author dqcer
 * @since 2023/01/14 18:01:66
 */
@RestController
@RequestMapping("log-operation")
public class LogOperationController {


    @Resource
    private ILogService logService;

    /**
     * 查询分页数据
     *
     * @param dto dto
     * @return {@link Result<PagedVO>}
     */
    @Transform
    @GetMapping("base/list")
    public Result<PagedVO<LogVO>> listByPage(@Validated(value = {ValidGroup.Paged.class}) LogLiteDTO dto){
        return logService.listByPage(dto);
    }
}
