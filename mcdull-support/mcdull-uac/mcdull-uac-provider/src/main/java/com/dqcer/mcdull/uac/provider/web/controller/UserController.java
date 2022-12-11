package com.dqcer.mcdull.uac.provider.web.controller;

import com.dqcer.framework.base.validator.ValidGroup;
import com.dqcer.framework.base.annotation.Transform;
import com.dqcer.framework.base.vo.PagedVO;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.uac.api.dto.UserLiteDTO;
import com.dqcer.mcdull.uac.api.vo.UserVO;
import com.dqcer.mcdull.uac.client.api.UserServiceApi;
import com.dqcer.mcdull.uac.provider.web.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController implements UserServiceApi {

    @Resource
    private UserService userService;

    /**
     * 列表
     *
     * @param dto dto
     * @return {@link Result}<{@link List}<{@link UserVO}>>
     */
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

    /**
     * 用户详情
     *
     * @param userId
     * @return {@link Long}
     */
    @Override
    public Result<UserVO> getDetail(Long userId) {
        UserLiteDTO dto = new UserLiteDTO();
        dto.setId(userId);
        return userService.detail(dto);
    }
}
