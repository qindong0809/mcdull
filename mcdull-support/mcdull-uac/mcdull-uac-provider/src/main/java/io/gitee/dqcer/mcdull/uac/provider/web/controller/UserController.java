package io.gitee.dqcer.mcdull.uac.provider.web.controller;

import io.gitee.dqcer.mcdull.uac.provider.model.dto.UserLiteDTO;
import io.gitee.dqcer.mcdull.uac.provider.model.vo.UserVO;
import io.gitee.dqcer.mcdull.framework.base.annotation.Authorized;
import io.gitee.dqcer.mcdull.framework.base.annotation.Transform;
import io.gitee.dqcer.mcdull.framework.base.validator.ValidGroup;
import io.gitee.dqcer.mcdull.framework.base.vo.PagedVO;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.redis.annotation.RedisLock;
import io.gitee.dqcer.mcdull.uac.provider.web.service.UserService;
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
 * @since 2022/12/25
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link UserVO}>>
     */
    @Authorized("sys:user:view")
    @GetMapping("user/base/page")
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
    public Result<Long> updateStatus(@RequestBody @Validated(value = {ValidGroup.Status.class}) UserLiteDTO dto){
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
