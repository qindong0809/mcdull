package com.dqcer.mcdull.uac.provider.web.api;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.uac.client.api.UserServiceApi;
import com.dqcer.mcdull.uac.client.vo.RemoteUserVO;
import com.dqcer.mcdull.uac.provider.web.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("")
@RestController
public class UserApi implements UserServiceApi {

    @Resource
    private UserService userService;

    /**
     * 用户详情
     *
     * @param userId
     * @return {@link Long}
     */
    @Override
    public Result<RemoteUserVO> getDetail(Long userId) {
//        UserLiteDTO dto = new UserLiteDTO();
//        dto.setId(userId);
//        return userService.detail(dto);
        return Result.ok();
    }
}
