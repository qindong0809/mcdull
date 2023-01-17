package io.gitee.dqcer.mcdull.framework.web.feign.service;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.web.feign.model.UserPowerVO;
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
    @PostMapping(GlobalConstant.INNER_API + "/user/res-module/list")
    Result<List<UserPowerVO>> queryResourceModules();

}
