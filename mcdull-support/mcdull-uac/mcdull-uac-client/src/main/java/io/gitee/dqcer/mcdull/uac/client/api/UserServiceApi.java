package io.gitee.dqcer.mcdull.uac.client.api;

import io.gitee.dqcer.mcdull.framework.base.constants.GlobalConstant;
import io.gitee.dqcer.mcdull.framework.base.wrapper.Result;
import io.gitee.dqcer.mcdull.framework.feign.ResultApi;
import io.gitee.dqcer.mcdull.uac.client.vo.RemoteUserVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 人员服务api
 *
 * @author dqcer
 * @since 2022/10/28
 */
public interface UserServiceApi {

    /**
     * 用户详情
     *
     * @param userId 用户id
     * @return {@link Result< RemoteUserVO >}
     */
    @PostMapping(GlobalConstant.INNER_API + "feign/user/base/detail")
    ResultApi<RemoteUserVO> getDetail(@RequestParam(value = "userId")Integer userId);

}
