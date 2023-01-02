package com.dqcer.mcdull.uac.provider.web.feign;

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

/**
 * 用户 feign 实现层
 *
 * @author dqcer
 * @version 2022/12/25
 */
@RestController
public class UserServerFeign implements UserServiceApi {

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
     * @param userId 用户id
     * @return {@link Result}<{@link RemoteUserVO}>
     */
    @Override
    public Result<RemoteUserVO> getDetail(Long userId) {
        return Result.ok();
    }
}
