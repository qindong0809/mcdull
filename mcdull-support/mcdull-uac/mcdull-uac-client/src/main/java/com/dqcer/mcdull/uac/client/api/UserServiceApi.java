package com.dqcer.mcdull.uac.client.api;

import com.dqcer.framework.base.wrapper.Result;
import com.dqcer.mcdull.uac.client.vo.RemoteUserVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 人员服务api
 *
 * @author dqcer
 * @version 2022/10/28
 */
public interface UserServiceApi {

    /**
     * 用户详情
     *
     * @return {@link Long}
     */
    @PostMapping("feign/user/base/detail")
    Result<RemoteUserVO> getDetail(@RequestParam(value = "userId")Long userId);

}
