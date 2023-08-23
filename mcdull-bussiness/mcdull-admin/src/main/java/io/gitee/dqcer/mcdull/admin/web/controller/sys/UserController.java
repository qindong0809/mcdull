package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserEditDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserEmailConfigDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserInsertDTO;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserDetailVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserEmailConfigVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserProfileVO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserVO;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IDeptService;
import io.gitee.dqcer.mcdull.admin.web.service.sys.IUserService;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.dto.StatusDTO;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.vo.TreeSelectVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.redis.annotation.RedisLock;
import io.gitee.dqcer.mcdull.framework.web.basic.BasicController;
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
@RestController
@RequestMapping("/system/user")
public class UserController implements BasicController {

    @Resource
    private IUserService userService;

    @Resource
    private IDeptService deptService;

    /**
     * 部门树列表
     *
     * @return {@link Result}
     */
    @GetMapping("deptTree")
    public Result<List<TreeSelectVO>> selectDeptTreeList() {
        return deptService.selectDeptTreeList();
    }

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link UserVO}>>
     */
    @Authorized("system:user:list")
    @GetMapping("list")
    public Result<PagedVO<UserVO>> paged(@Validated(ValidGroup.Paged.class) UserLiteDTO dto) {
        return userService.paged(dto);
    }

    /**
     * 单个
     *
     * @param userId userId
     * @return {@link Result}<{@link UserVO}>
     */
    @Authorized("system:user:query")
    @GetMapping({"/", "{userId}"} )
    public Result<UserDetailVO> detail(@PathVariable(value = "userId", required = false) Long userId) {
        return userService.detail(userId);
    }

    @GetMapping({"/profile"} )
    public Result<UserProfileVO> profile() {
        return userService.profile();
    }

    @PostMapping("email-config/update" )
    public Result<Boolean> updateEmailConfig(@RequestBody @Validated UserEmailConfigDTO dto) {
        return userService.updateEmailConfig(dto);
    }

    @GetMapping("email-config/detail" )
    public Result<UserEmailConfigVO> detailEmailConfig() {
        return userService.detailEmailConfig();
    }

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回新增主键}
     */
    @Authorized("system:user:add")
    @RedisLock(key = "'lock:uac:user:' + #dto.account", timeout = 3)
    @PostMapping()
    public Result<Long> add(@RequestBody UserInsertDTO dto){
        return userService.add(dto);
    }

    @Authorized("system:user:edit")
    @PutMapping()
    public Result<Long> edit(@RequestBody UserEditDTO dto){
        return userService.edit(dto);
    }

    /**
     * 状态更新
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @Authorized("system:user:edit")
    @PutMapping("changeStatus")
    public Result<Long> updateStatus(@RequestBody @Validated(value = {ValidGroup.Status.class}) StatusDTO dto){
        return userService.updateStatus(dto);
    }

    /**
     * 单个删除
     *
     * @param id id
     * @return {@link Result<Long>}
     */
    @Authorized("system:user:remove")
    @DeleteMapping("{id}")
    public Result<Long> delete(@PathVariable(value = "id")Long id){
        return userService.delete(id);
    }

    /**
     * 重置密码
     *
     * @param dto dto
     * @return {@link Result}<{@link Long}>
     */
    @PostMapping("user/reset-password/update")
    public Result<Long> resetPassword(@RequestBody @Validated(value = {ValidGroup.Update.class}) UserLiteDTO dto){
        return userService.resetPassword(dto);
    }

    @Authorized("system:user:export")
    @PostMapping("/export")
    public void export(@Validated(ValidGroup.Paged.class) UserLiteDTO dto) {
        userService.export(dto);
    }
}
