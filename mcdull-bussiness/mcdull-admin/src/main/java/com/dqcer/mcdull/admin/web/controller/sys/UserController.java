package io.gitee.dqcer.admin.web.controller.sys;

import io.gitee.dqcer.framework.base.annotation.Authorized;
import io.gitee.dqcer.framework.base.annotation.Transform;
import io.gitee.dqcer.framework.base.dto.StatusDTO;
import io.gitee.dqcer.framework.base.validator.ValidGroup;
import io.gitee.dqcer.framework.base.vo.PagedVO;
import io.gitee.dqcer.framework.base.wrapper.Result;
import io.gitee.dqcer.admin.config.OperationLog;
import io.gitee.dqcer.admin.config.OperationTypeEnum;
import io.gitee.dqcer.admin.model.dto.sys.UserLiteDTO;
import io.gitee.dqcer.admin.model.vo.sys.UserVO;
import io.gitee.dqcer.admin.web.service.sys.IUserService;
import io.gitee.dqcer.framework.redis.annotation.RedisLock;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户控制器
 *
 * @author dqcer
 * @version 2022/12/25
 */
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
    @GetMapping("user/base/list")
    @Transform
    public Result<PagedVO<UserVO>> listByPage(@Validated(ValidGroup.Paged.class) UserLiteDTO dto) {
        return userService.listByPage(dto);
    }

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result}<{@link UserVO}>
     */
    @GetMapping("user/base/detail")
    @Transform
    public Result<UserVO> detail(@Validated(ValidGroup.One.class) UserLiteDTO dto) {
        return userService.detail(dto);
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
    public Result<Long> insert(@RequestBody @Validated(value = {ValidGroup.Add.class})UserLiteDTO dto){
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
