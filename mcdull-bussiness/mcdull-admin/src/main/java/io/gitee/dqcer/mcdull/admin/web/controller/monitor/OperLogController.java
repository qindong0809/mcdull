package io.gitee.dqcer.mcdull.admin.web.controller.monitor;


import io.gitee.dqcer.mcdull.admin.model.dto.sys.LogLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.LoginInfoLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.LogVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.ILogService;
import io.gitee.dqcer.mcdull.admin.web.service.sys.ILoginInfoService;
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
 * 操作信息控制器
 *
 * @author dqcer
 * @since 2023/03/13
 */
@RequestMapping("/monitor/operlog")
@RestController
public class OperLogController {

    @Resource
    private ILogService logService;

    @Resource
    private ILoginInfoService loginInfoService;

    @Transform
//    @Authorized("monitor:operlog:list")
    @GetMapping("list")
    public Result<PagedVO<LogVO>> listByPage(@Validated(value = {ValidGroup.Paged.class}) LogLiteDTO dto){
        return logService.listByPage(dto);
    }

    /**
     * 导出excel
     *
     * @param dto dto
     */
    @GetMapping("export")
    public void exportExcel(@Validated(value = {ValidGroup.Export.class}) LoginInfoLiteDTO dto) {
        loginInfoService.exportExcel(dto);
    }
}
