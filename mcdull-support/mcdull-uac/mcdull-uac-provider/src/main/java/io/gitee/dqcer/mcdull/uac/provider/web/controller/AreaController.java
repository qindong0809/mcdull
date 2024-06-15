package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.AreaQueryDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.AreaVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IAreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author dqcer
 * @since 2024-06-15 13:11:44
 */
@RestController
@Tag(name = "行政区域")
public class AreaController {

    @Resource
    private IAreaService areaService;

    @Operation(summary = "分页")
    @PostMapping("/area/queryPage")
    public Result<PagedVO<AreaVO>> queryPage(@RequestBody @Valid AreaQueryDTO dto) {
        return Result.success(areaService.queryPage(dto));
    }


}