package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DatasourceAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DatasourceQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DatasourceTestDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.DatasourceUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.IdDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.StatusDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceOptionVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceTestVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IDatasourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据源配置控制器
 *
 * @author dqcer
 */
@Tag(name = "数据源配置")
@RestController
public class DatasourceController extends BasicController {

    @Resource
    private IDatasourceService datasourceService;

    @Operation(summary = "分页查询")
    @PostMapping("/system/datasource/query")
    @SaCheckEL("stp.checkPermission('system:datasource:query')")
    public Result<PagedVO<DatasourceVO>> query(@Validated @RequestBody DatasourceQueryDTO dto) {
        return Result.success(datasourceService.queryPage(dto));
    }

    @Operation(summary = "新增")
    @PostMapping("/system/datasource/add")
    @SaCheckEL("stp.checkPermission('system:datasource:write')")
    public Result<Boolean> add(@RequestBody @Valid DatasourceAddDTO dto) {
        datasourceService.add(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @PostMapping("/system/datasource/update")
    @SaCheckEL("stp.checkPermission('system:datasource:write')")
    public Result<Boolean> update(@RequestBody @Valid DatasourceUpdateDTO dto) {
        datasourceService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @PostMapping("/system/datasource/delete")
    @SaCheckEL("stp.checkPermission('system:datasource:delete')")
    public Result<Boolean> delete(@RequestBody @Valid IdDTO dto) {
        datasourceService.delete(dto.getId());
        return Result.success(true);
    }

    @Operation(summary = "启停")
    @PostMapping("/system/datasource/change-status")
    @SaCheckEL("stp.checkPermission('system:datasource:write')")
    public Result<Boolean> changeStatus(@RequestBody @Valid StatusDTO dto) {
        datasourceService.changeStatus(dto.getId(), dto.getInactive());
        return Result.success(true);
    }

    @Operation(summary = "下拉选项")
    @GetMapping("/system/datasource/options")
    @SaCheckEL("stp.checkPermission('system:datasource:query')")
    public Result<List<DatasourceOptionVO>> options() {
        return Result.success(datasourceService.options());
    }

    @Operation(summary = "测试连接")
    @PostMapping("/system/datasource/test-connection")
    @SaCheckEL("stp.checkPermission('system:datasource:test')")
    public Result<DatasourceTestVO> test(@RequestBody @Valid DatasourceTestDTO dto) {
        return Result.success(datasourceService.test(dto));
    }
}
