package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckEL;
import io.gitee.dqcer.mcdull.framework.base.exception.BusinessException;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.IdDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.StatusDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TenantAddDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TenantInitDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TenantMenuSaveDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TenantQueryDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TenantUpdateDTO;
import io.gitee.dqcer.mcdull.system.provider.model.dto.TenantUserDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.DatasourceOptionVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.MenuTreeVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantDetailVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantInitProgressVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantTaskVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantUserVO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.TenantVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.ITenantAuthService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ITenantInitService;
import io.gitee.dqcer.mcdull.system.provider.web.service.ITenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * 租户管理控制器
 *
 * @author dqcer
 */
@Tag(name = "租户管理")
@RestController
public class TenantController extends BasicController {

    @Resource
    private ITenantService tenantService;
    @Resource
    private ITenantAuthService tenantAuthService;
    @Resource
    private ITenantInitService tenantInitService;

    @Operation(summary = "分页查询")
    @PostMapping("/system/tenant/query")
    @SaCheckEL("stp.checkPermission('system:tenant:query')")
    public Result<PagedVO<TenantVO>> query(@RequestBody @Valid TenantQueryDTO dto) {
        return Result.success(tenantService.queryPage(dto));
    }

    @Operation(summary = "详情")
    @GetMapping("/system/tenant/detail")
    @SaCheckEL("stp.checkPermission('system:tenant:query')")
    public Result<TenantDetailVO> detail(@RequestParam("id") Integer id) {
        return Result.success(tenantService.detail(id));
    }

    @Operation(summary = "新增")
    @PostMapping("/system/tenant/add")
    @SaCheckEL("stp.checkPermission('system:tenant:write')")
    public Result<Boolean> add(@RequestBody @Valid TenantAddDTO dto) {
        tenantService.add(dto);
        return Result.success(true);
    }

    @Operation(summary = "更新")
    @PostMapping("/system/tenant/update")
    @SaCheckEL("stp.checkPermission('system:tenant:write')")
    public Result<Boolean> update(@RequestBody @Valid TenantUpdateDTO dto) {
        tenantService.update(dto);
        return Result.success(true);
    }

    @Operation(summary = "删除")
    @PostMapping("/system/tenant/delete")
    @SaCheckEL("stp.checkPermission('system:tenant:delete')")
    public Result<Boolean> delete(@RequestBody @Valid IdDTO dto) {
        tenantService.delete(dto.getId());
        return Result.success(true);
    }

    @Operation(summary = "启停")
    @PostMapping("/system/tenant/change-status")
    @SaCheckEL("stp.checkPermission('system:tenant:write')")
    public Result<Boolean> changeStatus(@RequestBody @Valid StatusDTO dto) {
        tenantService.changeStatus(dto.getId(), dto.getInactive());
        return Result.success(true);
    }

    @Operation(summary = "数据源下拉")
    @GetMapping("/system/tenant/datasource-options")
    @SaCheckEL("stp.checkPermission('system:tenant:query')")
    public Result<List<DatasourceOptionVO>> datasourceOptions() {
        return Result.success(tenantService.datasourceOptions());
    }

    @Operation(summary = "执行初始化")
    @PostMapping("/system/tenant/init")
    @SaCheckEL("stp.checkPermission('system:tenant:init')")
    public Result<TenantTaskVO> init(@RequestBody @Valid TenantInitDTO dto, HttpServletRequest request) {
        Integer currentTenantId = tenantAuthService.currentTenantId(request);
        if (currentTenantId != null) {
            tenantAuthService.validateCurrentTenant(currentTenantId);
        }
        tenantAuthService.validateCurrentTenant(dto.getTenantId());
        return Result.success(tenantInitService.start(dto.getTenantId(), dto.getForce()));
    }

    @Operation(summary = "初始化进度订阅")
    @GetMapping(value = "/system/tenant/init-progress", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @SaCheckEL("stp.checkPermission('system:tenant:init')")
    public SseEmitter initProgress(@RequestParam("taskNo") String taskNo,
                                   @RequestParam(value = "tid", required = false) String tid,
                                   @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId,
                                   HttpServletRequest request) {
        Integer tenantId = tenantAuthService.currentTenantId(request);
        if (tenantId == null && tid != null && !tid.isBlank()) {
            try {
                tenantId = Integer.valueOf(tid);
            } catch (NumberFormatException ex) {
                tenantId = null;
            }
        }
        tenantAuthService.validateCurrentTenant(tenantId);
        TenantInitProgressVO snapshot = tenantInitService.snapshot(taskNo);
        if (snapshot.getTenantId() != null && !snapshot.getTenantId().equals(tenantId)) {
            throw new BusinessException("system.tenant.forbidden");
        }
        Long last = null;
        if (lastEventId != null && !lastEventId.isBlank()) {
            try {
                last = Long.valueOf(lastEventId);
            } catch (NumberFormatException ignored) {
                last = null;
            }
        }
        return tenantInitService.progress(taskNo, last);
    }

    @Operation(summary = "初始化快照")
    @GetMapping("/system/tenant/init-snapshot")
    @SaCheckEL("stp.checkPermission('system:tenant:init')")
    public Result<TenantInitProgressVO> initSnapshot(@RequestParam("taskNo") String taskNo, HttpServletRequest request) {
        Integer tenantId = tenantAuthService.currentTenantId(request);
        if (tenantId == null) {
            throw new BusinessException("system.tenant.required");
        }
        tenantAuthService.validateCurrentTenant(tenantId);
        TenantInitProgressVO snapshot = tenantInitService.snapshot(taskNo);
        if (snapshot.getTenantId() != null && !snapshot.getTenantId().equals(tenantId)) {
            throw new BusinessException("system.tenant.forbidden");
        }
        return Result.success(snapshot);
    }

    @Operation(summary = "租户用户列表")
    @GetMapping("/system/tenant/user/list")
    @SaCheckEL("stp.checkPermission('system:tenant:write')")
    public Result<List<TenantUserVO>> listUsers(@RequestParam("tenantId") Integer tenantId) {
        return Result.success(tenantAuthService.listUsers(tenantId));
    }

    @Operation(summary = "添加租户用户")
    @PostMapping("/system/tenant/user/add")
    @SaCheckEL("stp.checkPermission('system:tenant:write')")
    public Result<Boolean> addUsers(@RequestBody @Valid TenantUserDTO dto) {
        tenantAuthService.addUsers(dto.getTenantId(), dto.getUserIds());
        return Result.success(true);
    }

    @Operation(summary = "移除租户用户")
    @PostMapping("/system/tenant/user/remove")
    @SaCheckEL("stp.checkPermission('system:tenant:write')")
    public Result<Boolean> removeUsers(@RequestBody @Valid TenantUserDTO dto) {
        tenantAuthService.removeUsers(dto.getTenantId(), dto.getUserIds());
        return Result.success(true);
    }

    @Operation(summary = "租户菜单树")
    @GetMapping("/system/tenant/menu/tree")
    @SaCheckEL("stp.checkPermission('system:tenant:write')")
    public Result<List<MenuTreeVO>> menuTree(@RequestParam("tenantId") Integer tenantId,
                                             @RequestParam(value = "applicationCode", required = false) String applicationCode) {
        return Result.success(tenantAuthService.menuTree(tenantId, applicationCode));
    }

    @Operation(summary = "保存租户菜单")
    @PostMapping("/system/tenant/menu/save")
    @SaCheckEL("stp.checkPermission('system:tenant:write')")
    public Result<Boolean> saveMenus(@RequestBody @Valid TenantMenuSaveDTO dto) {
        tenantAuthService.saveMenus(dto.getTenantId(), dto.getMenuIds());
        return Result.success(true);
    }
}
