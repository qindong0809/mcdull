package com.dqcer.mcdull.uac.provider.web.api;

import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.storage.UserContextHolder;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import com.dqcer.mcdull.uac.client.api.UserServiceApi;
import com.dqcer.mcdull.uac.client.vo.RemoteUserVO;
import com.dqcer.mcdull.uac.provider.web.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserApi implements UserServiceApi {

    @Resource
    private UserService userService;

    /**
     * 查询资源模块
     *
     * @return {@link Result}<{@link List}<{@link UserPowerVO}>>
     */
    @PostMapping(GlobalConstant.FEIGN_PREFIX + "/user/res-module/list")
    public Result<List<UserPowerVO>> queryResourceModules() {
        return userService.queryResourceModules(UserContextHolder.getSession().getUserId());
    }

    /**
     * 用户详情
     *
     * @param userId
     * @return {@link Long}
     */
    @Override
    public Result<RemoteUserVO> getDetail(Long userId) {
        return Result.ok();
    }
}
