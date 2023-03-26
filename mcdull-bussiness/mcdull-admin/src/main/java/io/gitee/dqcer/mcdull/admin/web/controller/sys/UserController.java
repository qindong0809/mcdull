package io.gitee.dqcer.mcdull.admin.web.controller.sys;

import io.gitee.dqcer.mcdull.admin.config.OperationLog;
import io.gitee.dqcer.mcdull.admin.config.OperationTypeEnum;
import io.gitee.dqcer.mcdull.admin.model.dto.sys.UserLiteDTO;
import io.gitee.dqcer.mcdull.admin.model.vo.sys.UserDetailVO;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private IDeptService deptService;

    /**
     * 部门树列表
     *
     * @return {@link Result}
     */
    @Authorized("system:user:list")
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
    public Result<PagedVO<UserVO>> listByPage(@Validated(ValidGroup.Paged.class) UserLiteDTO dto) {
        return userService.listByPage(dto);
    }

    /**
     * 单个
     *
     * @param userId userId
     * @return {@link Result}<{@link UserVO}>
     */
    @Authorized("system:user:query")
    @GetMapping("{userId}" )
    public Result<UserDetailVO> detail(@PathVariable(value = "userId", required = false) Long userId) {
        return userService.detail(userId);
    }

    /**
     * 新增数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回新增主键}
     */
    @OperationLog(module = "基础系统", menu = "用户管理", type = OperationTypeEnum.INSERT)
    @RedisLock(key = "'lock:uac:user:' + #dto.nickname + '-' + #dto.account", timeout = 3)
    @PostMapping("user/base/save")
    public Result<Long> insert(@RequestBody @Validated(value = {ValidGroup.Insert.class})UserLiteDTO dto){
        return userService.insert(dto);
    }

    /**
     * 更新数据
     *
     * @param dto dto
     * @return {@link Result<Long> 返回主键}
     */
    @PostMapping("user/base/update")
    public Result<Long> update(@RequestBody @Validated(value = {ValidGroup.Update.class})UserLiteDTO dto){
        return userService.update(dto);
    }

    /**
     * 状态更新
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @PostMapping("user/base/status")
    public Result<Long> updateStatus(@RequestBody @Validated(value = {ValidGroup.Status.class}) StatusDTO dto){
        return userService.updateStatus(dto);
    }

    /**
     * 单个删除
     *
     * @param dto dto
     * @return {@link Result<Long>}
     */
    @PostMapping("user/base/delete")
    public Result<Long> delete(@RequestBody @Validated(value = {ValidGroup.Delete.class}) UserLiteDTO dto){
        return userService.delete(dto);
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
}
