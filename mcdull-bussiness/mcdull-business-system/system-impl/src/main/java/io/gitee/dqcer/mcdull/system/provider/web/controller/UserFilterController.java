package io.gitee.dqcer.mcdull.system.provider.web.controller;

import cn.hutool.core.text.CharSequenceUtil;
import io.gitee.dqcer.mcdull.framework.base.storage.UnifySession;
import io.gitee.dqcer.mcdull.framework.base.storage.UserContextHolder;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
import io.gitee.dqcer.mcdull.system.provider.model.dto.UserFilterDTO;
import io.gitee.dqcer.mcdull.system.provider.model.vo.UserFilterVO;
import io.gitee.dqcer.mcdull.system.provider.web.service.IUserFilterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * user filter
 *
 * @author dqcer
 * @since 2026/02/09
 */
@Tag(name = "用户过滤信息")
@RestController
public class UserFilterController extends BasicController {

    @Resource
    private IUserFilterService userFilterService;

    @Operation(summary = "user filter list")
    @GetMapping("user/filter/list")
    public Result<List<UserFilterVO>> getList(@RequestParam(value = "categoryCode") String categoryCode,
                                              @RequestParam(value = "subCategoryCode", required = false) String subCategoryCode) {
        return Result.success(userFilterService.getList(UserContextHolder.userId(), UserContextHolder.getSession().getRoleId(), categoryCode, subCategoryCode));
    }

    @Operation(summary = "save user filter")
    @PostMapping("user/filter/save")
    public Result<Boolean> save(@RequestBody @Validated UserFilterDTO dto) {
        UnifySession session = UserContextHolder.getSession();
        String lockKey = CharSequenceUtil.format("user_filter_save:{}:{}", session.getUserId(), session.getRoleId());
        return Result.success(super.locker(lockKey, () -> userFilterService.save(UserContextHolder.userId(), session.getRoleId(), dto)));
    }

    @Operation(summary = "delete user filter")
    @PostMapping("user/filter/{id}/delete")
    public Result<Boolean> delete(@PathVariable("id") Integer id) {
        String lockKey = CharSequenceUtil.format("user_filter_delete:{}", id);
        return Result.success(super.locker(lockKey, () -> userFilterService.delete(id)));
    }

}
