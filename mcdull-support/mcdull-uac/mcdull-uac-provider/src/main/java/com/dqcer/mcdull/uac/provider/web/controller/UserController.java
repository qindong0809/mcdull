package com.dqcer.mcdull.uac.provider.web.controller;

import com.dqcer.framework.base.ValidGroup;
import com.dqcer.framework.base.dict.Transform;
import com.dqcer.framework.base.page.Paged;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.uac.api.dto.UserLiteDTO;
import com.dqcer.mcdull.uac.api.vo.UserVO;
import com.dqcer.mcdull.uac.provider.web.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("user")
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
    @GetMapping("base/page")
    @Transform
    public Result<Paged<UserVO>> listByPage(@Validated(ValidGroup.Paged.class) UserLiteDTO dto) {
        return userService.listByPage(dto);
    }

    /**
     * 单个
     *
     * @param dto dto
     * @return {@link Result}<{@link UserVO}>
     */
    @GetMapping("base/detail")
    public Result<UserVO> detail(@Validated(ValidGroup.One.class) UserLiteDTO dto) {
        return userService.detail(dto);
    }
}
