package com.dqcer.mcdull.framework.web.feign.service;

import com.dqcer.framework.base.constants.GlobalConstant;
import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 权限验证
 *
 * @author dqcer
 * @date 2022/12/17
 */
@FeignClient(name = "mcdull-uac-provider",  contextId = "permission")
public interface PowerCheckFeignClient {

    /**
     * 查询当前用户的角色、资源权限
     *
     * @return {@link Result}<{@link Boolean}>
     */
    @PostMapping(GlobalConstant.FEIGN_PREFIX + "/user/res-module/list")
    Result<List<UserPowerVO>> queryResourceModules();

}
