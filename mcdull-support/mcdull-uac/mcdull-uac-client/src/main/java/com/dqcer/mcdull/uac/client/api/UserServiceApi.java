package io.gitee.dqcer.uac.client.api;

import io.gitee.dqcer.framework.base.wrapper.Result;
import io.gitee.dqcer.uac.client.vo.RemoteUserVO;
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
     * @param userId 用户id
     * @return {@link Result<RemoteUserVO>}
     */
    @PostMapping("feign/user/base/detail")
    Result<RemoteUserVO> getDetail(@RequestParam(value = "userId")Long userId);

}
