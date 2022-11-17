package com.dqcer.mcdull.uac.client.api;

import com.dqcer.framework.base.constants.HttpHeaderConstants;
import com.dqcer.framework.base.wrapper.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登录服务api
 *
 * @author dongqin
 * @date 2022/10/28
 */
public interface AuthServiceApi {

    /**
     * 验证token
     *
     * @param token token
     * @return {@link Long}
     */
    @PostMapping("token/valid")
    Result<Long> tokenValid(@RequestParam(value = "token")String token, @RequestHeader(name = HttpHeaderConstants.TRACE_ID_HEADER) String traceId);

}
