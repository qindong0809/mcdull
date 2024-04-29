package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.redis.annotation.RedisLock;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserInsertDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserUpdateDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserUpdatePasswordDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.uac.provider.web.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户控制器
 *
 * @author dqcer
 * @since 2022/12/25
 */
@Tag(name = "User API")
@RestController
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link UserVO}>>
     */
    @Authorized("sys:user:view")
    @Operation(summary = "分页列表", description = "")
    @SaCheckPermission("system:user:query")
    @GetMapping("user/list")
    @RedisLock(key = "'lock:uac:user:' + #dto.pageSize ", timeout = 3)
//    @Transform
    public Result<PagedVO<UserVO>> listByPage(@Validated(ValidGroup.Paged.class) UserLiteDTO dto) {
//        ThreadUtil.sleep(8000);
        return Result.success(userService.listByPage(dto));
    }

    @Operation(summary = "新增数据", description = "重复控制")
    @RedisLock(key = "'lock:uac:user:' + #dto.nickname + '-' + #dto.account", timeout = 3)
    @PostMapping("user/insert")
    public Result<Long> insert(@RequestBody @Validated UserInsertDTO dto){
        return Result.success(userService.insert(dto));
    }

    @Operation(summary = "更新数据", description = "")
    @PutMapping("user/{id}/update")
    public Result<Long> update(@PathVariable("id") Long id,
                               @RequestBody @Validated UserUpdateDTO dto){
        return Result.success(userService.update(id, dto));
    }

    @Operation(summary = "切换状态", description = "")
    @Parameter(name = "id", required = true, description = "主键")
    @PutMapping("user/{id}/status")
    public Result<Long> toggleActive(@PathVariable("id") Long id) {
        return Result.success(userService.toggleActive(id));
    }

    @Operation(summary = "单个删除", description = "")
    @DeleteMapping("user/{id}/delete")
    public Result<Boolean> delete(@PathVariable("id") Long id){
        return Result.success(userService.delete(id));
    }

    @Operation(summary = "修改密码", description = "")
    @PostMapping("user/{id}/update-password")
    public Result<Long> updatePassword(@PathVariable("id") Long id,
                                       @RequestBody UserUpdatePasswordDTO dto){
        return Result.success(userService.updatePassword(id, dto));
    }
}
